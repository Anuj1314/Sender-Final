package com.darbyTelematics.Sender.repo;

public class PublicKeys {
    int id;
    String keyData;

    public PublicKeys(int id, String keyData) {
        this.id = id;
        this.keyData = keyData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData;
    }

    public PublicKeys() {
    }
}
