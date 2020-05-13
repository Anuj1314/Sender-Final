package com.darbyTelematics.Sender.service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

public class EncryptionService {
    private Cipher cipher;
    public EncryptionService() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("RSA");
    }

    public String encryptText(String msg, PublicKey key) throws InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {

        this.cipher.init(Cipher.ENCRYPT_MODE,key);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
    }

    public String decryptText(String msg, PrivateKey key) throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(cipher.doFinal(decoder.decode(msg)), "UTF-8");
    }
    //    public  void encryptAndSend(String myValue) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//        System.out.println("At client, before encr" + myValue);
//        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(getFileInBytes(new File("PublicKeys/KeyOf2")));
//        PublicKey newKey = null;
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        newKey = kf.generatePublic(X509publicKey);
//        String encryptedMessage = encryptText(myValue,newKey);
//
//        System.out.println("At client, after encr" + encryptedMessage);
//        restTemplate.postForObject("http://localhost:9092/storeMessage",encryptedMessage,String.class);
//    }
//
//    public byte[] getFileInBytes(File f) throws IOException {
//        FileInputStream fis = new FileInputStream(f);
//        byte[] fbytes = new byte[(int)f.length()];
//        System.out.println("length of file is "+f.length());
//        fis.read(fbytes);
//        fis.close();
//        return fbytes;
//    }
}
