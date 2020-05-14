package com.darbyTelematics.Sender.service;

import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class GenerateKeys {
    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keyLength) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPairGenerator.initialize(keyLength);

        this.keyPair = this.keyPairGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "GenerateKeys{" +
                "keyPairGenerator=" + keyPairGenerator +
                ", keyPair=" + keyPair +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}