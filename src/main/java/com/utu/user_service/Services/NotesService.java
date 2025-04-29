package com.utu.user_service.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utu.user_service.Models.FileMetaDataDoc;
import com.utu.user_service.Models.OutBoxEvent;
import com.utu.user_service.Models.User;
import com.utu.user_service.Repositories.NotesRepository;
import com.utu.user_service.Repositories.OutBoxRepository;
import com.utu.user_service.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final UserRepository userRepository;
    private final OutBoxRepository outboxRepository;
    private final NotesRepository notesRepository;

    public void saveFile(MultipartFile file) {
        try {
            String username = "Ravii"; // ideally should be dynamic
            String folderId = "681066469ca8b71c4bc99dc4";
            User user = userRepository.findByUsername(username);
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            Long size = file.getSize();

            // Creating metadata for the file
            FileMetaDataDoc metadata = FileMetaDataDoc.builder()
                    .fileName(originalFilename)
                    .fileType(contentType)
                    .userId(user.getId())
                    .folderId(folderId)
                    .size(size)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            notesRepository.save(metadata);
            List<String> filesMap = user.getFilesMap();
            if(filesMap==null){
                filesMap=new ArrayList<>();
            }

           filesMap.add(metadata.getId().toHexString());
            user.setFilesMap(filesMap);
            userRepository.save(user);
          //  notesRepository.save(metadata);
            // Save Outbox Event for this file upload
            String payload = new ObjectMapper().writeValueAsString(Map.of(
                    "fileName", originalFilename,
                    "contentType", contentType,
                    "userId", user.getId(),
                    "folderId", folderId,
                    "fileBytes", Base64.getEncoder().encodeToString(file.getBytes())
            ));
            OutBoxEvent event = OutBoxEvent.builder()
                    .eventType("FILE_UPLOAD")
                    .payload(payload)
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();
            outboxRepository.save(event);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving file", e);
        }
    }
}