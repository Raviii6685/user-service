package com.utu.user_service.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "user")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String college;
    private String website;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private UserAuth userAuth;

    private String description;

    private String techSkills;

    private List<String> folders;
    private List<String> filesMap; // Storing Map as JSON String // folderId -> files
    private List<String> summaries;
    private List<String> questions;
}
