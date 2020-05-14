package com.darbyTelematics.Sender.controller;

import com.darbyTelematics.Sender.service.*;
import com.pojo.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
public class ControllerClient {
    @Autowired SharePublicKeyService sharePublicKeyService;
    @Autowired SendMessageService sendMessageService;
    @Autowired GetMessageService getMessageService;

    @GetMapping("/connect")
    public String storePublicKeyToDb(){
        return sharePublicKeyService.SaveToDataBase();
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody Messages myMessage) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException {
        return sendMessageService.sendToListener(myMessage);
    }

    @GetMapping(value = "/getMessages")
    public List<String> getMessages() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        return getMessageService.getAllMessages();
    }
}
