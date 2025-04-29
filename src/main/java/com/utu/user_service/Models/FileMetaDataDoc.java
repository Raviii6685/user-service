package com.utu.user_service.Models;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "file_metadata")
@Data
@Builder
public class FileMetaDataDoc {
    @Id
    private ObjectId id;
    private Long userId;
    private String folderId;
    private String fileName;
    private String fileType;
    private Long size;
    private LocalDateTime uploadedAt;
}