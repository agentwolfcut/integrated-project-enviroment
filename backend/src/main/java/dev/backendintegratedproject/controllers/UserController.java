package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.userManage.LoginRequest;
import dev.backendintegratedproject.userManage.UserEntity;
import dev.backendintegratedproject.services.UserService;
import dev.backendintegratedproject.userManage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/v2/login")

@CrossOrigin(origins = {"http://localhost:5173",
        "http://ip23ft.sit.kmutt.ac.th",
        "http://intproj23.sit.kmutt.ac.th"})

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


//    @PostMapping()
//    public ResponseEntity<Void> loginUser(@RequestBody LoginRequest loginRequest) {
//        boolean isValidUser = userService.validateUser(loginRequest.getUserName(), loginRequest.getPassword());
//        if (isValidUser) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @GetMapping()
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        List<String> errors = new ArrayList<>();

        if (loginRequest.getUserName() == null || loginRequest.getUserName().isEmpty()) {
            errors.add("Username cannot be empty");
        } else if (loginRequest.getUserName().length() > 50) {
            errors.add("Username must be at most 50 characters long");
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            errors.add("Password cannot be empty");
        } else if (loginRequest.getPassword().length() > 14) {
            errors.add("Password must be at most 14 characters long");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join(", ", errors));
        }

        UserEntity user = userRepository.findByUserName(loginRequest.getUserName());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The username or password is incorrect.");
        }
    }

}
