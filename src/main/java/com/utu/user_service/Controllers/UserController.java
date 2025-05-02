package com.utu.user_service.Controllers;


import com.utu.user_service.Models.CreateUserDTO;
import com.utu.user_service.Models.LoginUserDTO;
import com.utu.user_service.Models.UpdateProfileDTO;
import com.utu.user_service.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody CreateUserDTO createUserDTO){
        try{
            userService.createUser(createUserDTO);
            log.info("user save with username: {}",createUserDTO.getUsername());
            return new ResponseEntity<>("UserCreated",HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("user saving failed due to ",e.toString());
            return new ResponseEntity<>("Usernot saved try again", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> longIn(@RequestBody LoginUserDTO loginuserDTO){
        try{
            userService.loginUser(loginuserDTO);
            return new ResponseEntity<>("login successfull",HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("login Faild",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(){
        return new ResponseEntity<>("logout",HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDTO updateProfileDTO){
        try{
            userService.updateProfile(updateProfileDTO);
            return new ResponseEntity<>("profile Updated",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("not able to update",HttpStatus.OK);
        }
    }

}
