package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.managements.entities.UserMainEntity;
import dev.backendintegratedproject.managements.repositories.UserMainRepository;
import dev.backendintegratedproject.securities.JwtTokenUtil;
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
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/v3")
@CrossOrigin(origins = {"http://localhost:5173", "http://ip23kk3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserMainRepository userMainRepository;

    @GetMapping()
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        List<String> errors = new ArrayList<>();

        // Validate username
        if (loginRequest.getUserName() == null || loginRequest.getUserName().isEmpty()) {
            errors.add("Username cannot be empty");
        } else if (loginRequest.getUserName().length() > 50) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Username must be at most 50 characters long.");
        }

        // Validate password
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            errors.add("Password cannot be empty");
        } else if (loginRequest.getPassword().length() > 14) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Password must be at most 14 characters long.");
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty() && loginRequest.getUserName() == null || loginRequest.getUserName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Username and password cannot be empty"));
        }


        if (!errors.isEmpty()) {
            ResponseEntity.badRequest().body(Map.of("message", String.join(", ", errors)));
        }

        UserEntity user = userRepository.findByUserName(loginRequest.getUserName());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            UserMainEntity userMainEntity = (UserMainEntity) userMainRepository.findByOid(user.getOid()).orElse(null);
            if (userMainEntity == null) {
                userMainEntity = new UserMainEntity();
                userMainEntity.setOid(user.getOid());
                userMainEntity.setName(user.getName());
                userMainEntity.setUserName(user.getUserName());
                userMainEntity.setEmail(user.getEmail());
                userMainRepository.save(userMainEntity);
            }

            // Generate JWT token
            String token = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok(Map.of("access_token", token));
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "The username or password is incorrect."));
        }
    }
}