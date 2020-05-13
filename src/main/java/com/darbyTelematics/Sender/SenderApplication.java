package com.darbyTelematics.Sender;

import com.darbyTelematics.Sender.service.EncryptionService;
import com.darbyTelematics.Sender.service.GenerateKeys;
import com.darbyTelematics.Sender.service.UDPClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SenderApplication {
	public static void main(String[] args) {
		SpringApplication.run(SenderApplication.class, args);
	}

	@Bean
	UDPClient getUDPClient(){
		return new UDPClient();
	}
	@Bean
	GenerateKeys getGenerateKeys() throws NoSuchAlgorithmException {
		return new GenerateKeys(1024);
	}
	@Bean
	EncryptionService getEncryptionService() throws NoSuchAlgorithmException, NoSuchPaddingException {
		return new EncryptionService();
	}

}
