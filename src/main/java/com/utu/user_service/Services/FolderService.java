package com.utu.user_service.Services;

import com.utu.user_service.Models.CreateFolderDTO;
import com.utu.user_service.Models.Folder;
import com.utu.user_service.Models.User;
import com.utu.user_service.Repositories.FolderRepository;
import com.utu.user_service.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderService {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public void createFolder(CreateFolderDTO createFolderDTO) {
        try {
            String username = "Ravii";
            User user = userRepository.findByUsername(username);
            List<String> folders = user.getFolders();

            if (folders == null) {
                folders = new ArrayList<>();
            }

            // Create parent folder
            Folder parentFolder = new Folder();
            parentFolder.setFolderName(createFolderDTO.getFolderName());
            parentFolder.setCanDelete(true); // Just example, can be customized
            parentFolder.setCanModify(true); // Just example, can be customized

            List<Folder> parentSubFolders = new ArrayList<>();
            List<String> subFolderNames = Arrays.asList("1_day", "3_day", "7_day", "15_day", "30_day");

            for (String subFolderName : subFolderNames) {
                Folder subFolder = new Folder();
                subFolder.setFolderName(subFolderName);
                subFolder.setCanDelete(false);  // Example: Read-only subfolders
                subFolder.setCanModify(false);  // Example: Read-only subfolders
                folderRepository.save(subFolder); // Save subfolder to DB

                parentSubFolders.add(subFolder);  // Add the subfolder as reference (not ID)
            }

            parentFolder.setSubFolders(parentSubFolders);  // Set the subfolders in parent folder
            folderRepository.save(parentFolder);  // Save parent folder

            folders.add(parentFolder.getId().toHexString());  // Add parent folder reference to user's folders
            user.setFolders(folders);  // Update user with the new folders list

            userRepository.save(user);  // Save updated user
            log.info("Folder saved successfully");

        } catch (Exception e) {
            log.error("Error saving folder", e);
        }
    }


    public Optional<Folder> openFolder(ObjectId folderId) {
        return folderRepository.findById(folderId);
    }

    public void deleteFolder(ObjectId folderId) {
        try {
            // Find the folder to delete by its ID
            String username = "Ravii";
            User user= userRepository.findByUsername(username);
            List<String> folders =  user.getFolders();
            folders.remove(folderId.toHexString());
            user.setFolders(folders);
            userRepository.save(user);
            Folder folderToDelete = folderRepository.findById(folderId)
                    .orElseThrow(() -> new RuntimeException("Folder not found"));

            // Get the subfolders associated with the folder to delete
            List<Folder> subFolders = folderToDelete.getSubFolders();

            // Delete all subfolders first
            if (subFolders != null) {
                for (Folder subFolder : subFolders) {
                    folderRepository.deleteById(subFolder.getId());  // Delete each subfolder
                }
            }

            // Now delete the parent folder itself
            folderRepository.deleteById(folderId);
            log.info("Folder and its subfolders deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting folder and subfolders", e);
        }
    }
}