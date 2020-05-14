package com.darbyTelematics.Sender.service;

import com.darbyTelematics.Sender.model.PublicKeys;
import com.darbyTelematics.Sender.repo.MyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SharePublicKeyService {
    @Value("${whoAmI}")
    String mySelf;
    @Autowired MyRepo myRepo;
    @Autowired GenerateKeys generateKeys;


    public String SaveToDataBase(){
        Base64.Encoder b64e = Base64.getEncoder();
        String encodedPublicKey = new String(b64e.encode(generateKeys.getPublicKey().getEncoded()), StandardCharsets.UTF_8);
        myRepo.save(new PublicKeys(Integer.parseInt(mySelf),encodedPublicKey));
        return "Key Shared On Public Database";
    }
}
