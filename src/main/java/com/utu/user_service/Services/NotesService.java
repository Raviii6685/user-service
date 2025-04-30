package com.utu.user_service.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.notes.grpc.responseFileUpload;
import com.utu.user_service.Configs.GrpcClientt;
import com.utu.user_service.Models.FileMetaDataDoc;
import com.utu.user_service.Models.OutBoxEvent;
import com.utu.user_service.Models.User;
import com.utu.user_service.Repositories.NotesRepository;
import com.utu.user_service.Repositories.OutBoxRepository;
import com.utu.user_service.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final MongoTemplate mongoTemplate;
    private final GrpcClientt grpcClientt;

    public void saveFile(MultipartFile file) {
        ClientSessionOptions options = ClientSessionOptions.builder().build();
        ClientSession clientSession = mongoTemplate.getMongoDatabaseFactory().getSession(options);
        try {
            clientSession.startTransaction();
            String username = "Ravii"; // ideally should be dynamic
            String folderId = "681221275a411b77876a9df4";
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

            mongoTemplate.save(metadata);


            List<String> filesMap = user.getFilesMap();
            if(filesMap==null){
                filesMap=new ArrayList<>();
            }

           filesMap.add(metadata.getId().toHexString());
            user.setFilesMap(filesMap);


          //  notesRepository.save(metadata);
            // Save Outbox Event for this file upload
            Map<String ,Object> payload = Map.of(
                    "fileName", originalFilename,
                    "contentType", contentType,
                    "userId", user.getId(),
                    "folderId", folderId,
                    "fileBytes", Base64.getEncoder().encodeToString(file.getBytes())
            );
//            OutBoxEvent event = OutBoxEvent.builder()
//                    .eventType("FILE_UPLOAD")
//                    .payload(payload)
//                    .status("PENDING")
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            outboxRepository.save(event);
            responseFileUpload grpcResponse = grpcClientt.sendFileToNotesService(payload);

            if (!grpcResponse.getStatus().equalsIgnoreCase("Success")) {
                throw new RuntimeException("gRPC failed");
            }
            System.out.println(grpcResponse.getFileId());
            user.getFilesMap().add(grpcResponse.getFileId());
            userRepository.save(user);
            clientSession.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving file", e);
        }
    }

    public byte[] getFile(){
        String fileId="68122387addb8475e00de172";
        try{
            byte[]  dummy =grpcClientt.getFileForUser(fileId);
           return dummy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}