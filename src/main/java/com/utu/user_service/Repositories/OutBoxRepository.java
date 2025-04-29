package com.utu.user_service.Repositories;

// package: com.utu.user_service.Repositories

import com.utu.user_service.Models.OutBoxEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxRepository extends MongoRepository<OutBoxEvent, String> {
    List<OutBoxEvent> findByStatus(String status);
}