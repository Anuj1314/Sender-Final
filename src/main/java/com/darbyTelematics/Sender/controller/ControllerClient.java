package com.darbyTelematics.Sender.controller;

import com.darbyTelematics.Sender.repo.*;
import com.darbyTelematics.Sender.service.EncryptionService;
import com.darbyTelematics.Sender.service.GenerateKeys;
import com.darbyTelematics.Sender.service.UDPClient;
import com.pojo.Messages;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.ListIterator;


@RestController
public class ControllerClient {
    @Autowired
    MyRepo myRepo;

    @Autowired
    MyRepoForGettingMessages myRepoForGettingMessages;

    @Autowired
    GenerateKeys generateKeys;

    @Autowired
    EncryptionService encryptionService;


    @Autowired
    UDPClient udpClient;

//    @GetMapping("/sendPublicKey")
//    public void sendKeys() throws IOException {
//        System.out.println(generateKeys.getPublicKey()+"\n\n"+generateKeys.getPrivateKey());
//        storeAndSendKeys.storeLocallySendPublically();
//    }
//
//    @GetMapping("/getAllPublicKeys")
//    public void getAllPublicKeysFromServer() throws IOException {
//        storeAndSendKeys.getKeysFromServer();
//    }
//
    @GetMapping("/connect")
    public void storePublicKeyToDb(){
        Base64.Encoder b64e = Base64.getEncoder();
        String encodedPublicKey = new String(b64e.encode(generateKeys.getPublicKey().getEncoded()), StandardCharsets.UTF_8);
        myRepo.save(new PublicKeys(1,encodedPublicKey));

    }

    @PostMapping("/sendMessage")
    public  void sendMessage(@RequestBody Messages myMessage) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException {

//      getting the key of another-end
        PublicKeys pk = myRepo.findById(myMessage.getReceiver()).get();
        String keyToBeDecoded = pk.getKeyData();
        Base64.Decoder b64d = Base64.getDecoder();

//        key will be in String format, need to convert it back to PublicKey object
        byte[] byteKey = b64d.decode(keyToBeDecoded);
        PublicKey publicKey = null;
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        publicKey = kf.generatePublic(X509publicKey);

//      Encrypting the message
        String encryptedMessage = encryptionService.encryptText(myMessage.getData(),publicKey);


//      sending data to Listener
        udpClient.run(new Messages(myMessage.getSender(),myMessage.getReceiver(),encryptedMessage));

    }

    @GetMapping("/getMessage")
    public void getMessages() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        List<Messages> al;
        al = myRepoForGettingMessages.findByReceiver(1);
        ListIterator iterator = al.listIterator();
        while (iterator.hasNext()) {
            Messages messages = (Messages) iterator.next();
            String me = encryptionService.decryptText(messages.getData(), generateKeys.getPrivateKey());
            System.out.println("message I got is ::>> " + me);
        }
    }


}
