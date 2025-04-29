package com.utu.user_service.Models;


import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String username;

    private String email;
    private String college;
    private String password;
    private String techSkills;
    private String website;



}
