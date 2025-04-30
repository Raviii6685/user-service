package com.utu.user_service.Configs;


import com.google.protobuf.ByteString;
import com.notes.grpc.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
public class GrpcClientt {

    ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext()
            .build();

    notesServiceGrpc.notesServiceBlockingStub notesStub = notesServiceGrpc.newBlockingStub(channel);





    public responseFileUpload sendFileToNotesService(Map<String, Object> payload) {
        try {
            // Step 1: Payload se values nikalo
            String fileName = (String) payload.get("fileName");
            String fileType = (String) payload.get("contentType");
            String userId =  payload.get("userId").toString();
            String folderId = (String) payload.get("folderId");
            String fileBytesBase64 = (String) payload.get("fileBytes");
            byte[] fileBytes = Base64.getDecoder().decode(fileBytesBase64);



            requestFileUpload request = requestFileUpload.newBuilder()
                    .setFileName(fileName)
                    .setFileType(fileType)
                    .setUserId(userId)
                    .setFolderId(folderId)
                    .setContent(ByteString.copyFrom(fileBytes))
                    .build();


            responseFileUpload response = notesStub.uploadFile(request);
            log.info("gRPC Response: Status={}, Message={}", response.getStatus(), response.getMessage());
            return response;
        } catch (Exception e) {
            log.error("gRPC call failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send file to Notes Service via gRPC");
        }
    }

    public byte[] getFileForUser(String fileId) {
        try{
            FileDownloadRequest fileDownloadResquest = FileDownloadRequest.newBuilder()
                    .setFileId(fileId)
                    .build();
            FileDownloadResponse fileDownloadResponse =notesStub.getFile(fileDownloadResquest);
            byte[] contentBytes = fileDownloadResponse.getContent().toByteArray();

            return contentBytes;
        }catch (Exception e){
            return null;
        }
    }
}