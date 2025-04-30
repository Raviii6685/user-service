package com.utu.user_service.Controllers;


import com.utu.user_service.Models.FileMetadata;
import com.utu.user_service.Services.NotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class NotesController {
    private final NotesService notesService;
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        try{
            notesService.saveFile(file);
            log.info("file saved successfully");
            return new ResponseEntity<>("file has been saved", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("File is not saved",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get")
    public ResponseEntity<byte[]> getFile(){
        try{
            return new ResponseEntity<>(notesService.getFile(),HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
