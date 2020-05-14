package com.darbyTelematics.Sender.repo;
import com.darbyTelematics.Sender.model.PublicKeys;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MyRepo extends MongoRepository<PublicKeys, Integer> {
}
