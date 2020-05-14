package com.darbyTelematics.Sender.service;

import com.darbyTelematics.Sender.model.PublicKeys;
import com.darbyTelematics.Sender.repo.MyRepo;
import com.pojo.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SendMessageService {
    @Autowired EncryptionService encryptionService;
    @Autowired UDPClient udpClient;
    @Autowired MyRepo myRepo;
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
