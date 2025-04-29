package com.utu.user_service.Controllers;


import com.utu.user_service.Models.CreateFolderDTO;
import com.utu.user_service.Models.Folder;
import com.utu.user_service.Services.FolderService;
import com.utu.user_service.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;
    @PostMapping("/createFolder")
    public void createFolder(@RequestBody CreateFolderDTO createFolderDTO){
        try{
            folderService.createFolder(createFolderDTO);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }

    @GetMapping("/openFolder/{folderId}")
    public Optional<Folder> openFolder(@PathVariable("folderId") ObjectId folderId){
        try{
            return folderService.openFolder(folderId);

        }catch (Exception e){
           return null;
        }

    }
    @GetMapping("/homeFolder")
    public List<String> homeFolders(){
        try{
            String username = "Ravii";
            return userService.getFolders(username);


        } catch (Exception e) {
            log.info("cant open the fodler");
            return null;
        }
    }

    @DeleteMapping("delete/{folderId}")
    public boolean deleteFolder(@PathVariable("folderId") ObjectId folderId){
        try{
            folderService.deleteFolder(folderId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
