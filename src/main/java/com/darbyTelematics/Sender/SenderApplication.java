package com.darbyTelematics.Sender;

import com.darbyTelematics.Sender.service.GenerateKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SenderApplication {
	public static void main(String[] args) {
		SpringApplication.run(SenderApplication.class, args);
	}
	@Bean
	GenerateKeys getGenerateKeys() throws NoSuchAlgorithmException {
		return new GenerateKeys(1024);
	}
}


