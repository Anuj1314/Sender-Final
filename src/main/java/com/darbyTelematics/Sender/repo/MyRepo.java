package com.darbyTelematics.Sender.repo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MyRepo extends MongoRepository<PublicKeys, Integer> {
}
