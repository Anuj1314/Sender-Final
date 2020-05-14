package com.darbyTelematics.Sender.service;

import com.darbyTelematics.Sender.repo.MyRepoForGettingMessages;
import com.pojo.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class GetMessageService {
    @Value("${whoAmI}")
    String mySelf;
    @Autowired MyRepoForGettingMessages myRepoForGettingMessages;
    @Autowired EncryptionService encryptionService;
    @Autowired GenerateKeys generateKeys;
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
}

