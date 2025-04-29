package com.utu.user_service.Services;

import com.utu.user_service.Models.*;
import com.utu.user_service.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public void createUser(CreateUserDTO createUserDTO) throws Exception {
        try{
            UserAuth userauth = UserAuth.builder()
                    .username(createUserDTO.getUsername())
                    .password(createUserDTO.getPassword()).build();
            User user = User.builder()
                    .userAuth(userauth)
                    .email(createUserDTO.getEmail())
                    .build();
            userRepository.save(user);

        }catch (Exception e){
            log.info("error in user-service");
            throw new Exception("error in saving the user");
        }

    }

    public void loginUser(LoginUserDTO loginuserDTO) {
        log.info("login successful");
    }
    public void logout(){
        log.info("Logout sucessful");
    }

    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        try{

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User mapperToDTO(UpdateProfileDTO updateProfileDTO){
       User user = new User();

       userRepository.findByUsername(updateProfileDTO.getUsername());
       return user;
    }

    public List<String> getFolders(String username) {
        return userRepository.findByUsername(username).getFolders();
    }
}
