package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.entities.UserEntity;
import dev.backendintegratedproject.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin(origins = {"http://localhost:5173",
                        "http://ip23ft.sit.kmutt.ac.th",
                        "http://intproj23.sit.kmutt.ac.th"})
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestParam String username, @RequestParam String password) {
        UserEntity user = userService.login(username, password);
        return ResponseEntity.ok(user);
    }

}
