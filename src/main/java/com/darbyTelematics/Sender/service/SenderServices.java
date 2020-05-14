package com.darbyTelematics.Sender.service;

import com.darbyTelematics.Sender.model.PublicKeys;
import com.darbyTelematics.Sender.repo.MyRepo;
import com.darbyTelematics.Sender.repo.MyRepoForGettingMessages;
import com.pojo.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ListIterator;

@Service
public class SenderServices {
    @Value("${whoAmI}")
    String mySelf;

    @Autowired
    MyRepo myRepo;

    @Autowired
    GenerateKeys generateKeys;

    @Autowired
    EncryptionService encryptionService;

    @Autowired
    MyRepoForGettingMessages myRepoForGettingMessages;

    @Autowired
    UDPClient udpClient;

    public String SaveToDataBase(){
        Base64.Encoder b64e = Base64.getEncoder();
        String encodedPublicKey = new String(b64e.encode(generateKeys.getPublicKey().getEncoded()), StandardCharsets.UTF_8);
        myRepo.save(new PublicKeys(Integer.parseInt(mySelf),encodedPublicKey));
        return "Key Shared On Public Database";
    }

    public List<String> getAllMessages() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        List<Messages> allMessages = myRepoForGettingMessages.findByReceiver(Integer.parseInt(mySelf));
        ListIterator iterator = allMessages.listIterator();
        List<String> allReadableMessages = new ArrayList<>();
        while (iterator.hasNext()) {
            Messages messages = (Messages) iterator.next();
            String me = encryptionService.decryptText(messages.getData(), generateKeys.getPrivateKey());
            allReadableMessages.add(me);
            System.out.println("message I got is ::>> " + me);

        }
        return allReadableMessages;
    }

    public String sendToListener(Messages message) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ClassNotFoundException {
//      getting the key of another-end
        PublicKeys pk = myRepo.findById(message.getReceiver()).get();
        String keyToBeDecoded = pk.getKeyData();
        Base64.Decoder b64d = Base64.getDecoder();

//      key will be in String format, need to convert it back to PublicKey object
        byte[] byteKey = b64d.decode(keyToBeDecoded);
        PublicKey publicKey = null;
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        publicKey = kf.generatePublic(X509publicKey);

        //      Encrypting the message
        String encryptedMessage = encryptionService.encryptText(message.getData(),publicKey);

        //      sending data to Listener
        return udpClient.run(new Messages(message.getSender(),message.getReceiver(),encryptedMessage));
    }
}
