package com.utu.user_service.Models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class FileMetadata {
    private String fileId;        // GridFS ID
    private String fileName;
    private String fileType;
    private Long size;
    private String folderId;
    private LocalDateTime uploadedAt;
}