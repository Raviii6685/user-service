package com.utu.user_service.Models;


import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("folder_service")
@Data
public class Folder {

    private ObjectId id;
    @Indexed(unique = true)
    private String folderName;
    //private ObjectId isParent = null;
    private Boolean canDelete = true;
    private Boolean canModify = true;
   // private Long MetaData;
   @DBRef // This will reference other Folder objects, not just their String IDs
   private List<Folder> subFolders = new ArrayList<>();
    private List<String> files;

}
