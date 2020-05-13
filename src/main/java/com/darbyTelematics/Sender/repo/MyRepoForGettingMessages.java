package com.darbyTelematics.Sender.repo;

import com.pojo.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MyRepoForGettingMessages extends MongoRepository<Messages,Integer> {

    @Query("{'receiver': ?0}")
    List<Messages> findByReceiver(int receiver);
}
