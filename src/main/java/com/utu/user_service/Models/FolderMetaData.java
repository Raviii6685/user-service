package com.utu.user_service.Models;


import jakarta.persistence.*;

@Entity
@Table(name="folder_meta_data")
public class FolderMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String folderName;
    private Integer files;

}
