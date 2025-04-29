package com.utu.user_service.Repositories;

import com.utu.user_service.Models.FileMetaDataDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends MongoRepository<FileMetaDataDoc, ObjectId> {
}
