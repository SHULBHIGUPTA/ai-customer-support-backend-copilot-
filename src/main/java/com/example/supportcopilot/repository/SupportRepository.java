package com.example.supportcopilot.repository;

import com.example.supportcopilot.model.SupportChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends MongoRepository<SupportChat, String> {
}