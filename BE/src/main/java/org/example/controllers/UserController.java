package org.example.controllers;

import org.example.models.User;
import org.example.objects.FriendDTO;
import org.example.objects.UserDTO;
import org.example.repositories.UserRepository;
import org.example.requests.CreateUserRequest;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    public String username;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public String loggedInUserDetails(Principal principal) {
        username=principal.getName();
        return principal.getName();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);

        UserDTO responseUser = new UserDTO(user.getName(), user.getUsername(), user.getRoles());

        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
    }
    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(@RequestBody FriendDTO friendDTO){
        userService.addFriend(friendDTO.getUsername1(),friendDTO.getUsername2());
        System.out.println(friendDTO.getUsername1());
        return new ResponseEntity<>("Friend added", HttpStatus.CREATED);
    }
}
