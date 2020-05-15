package com.darbyTelematics.Sender.service;

import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

@Service
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


    public byte[] getFileInBytes(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] fbytes = new byte[(int)f.length()];
        System.out.println("length of file is "+f.length());
        fis.read(fbytes);
        fis.close();
        return fbytes;
    }
}
