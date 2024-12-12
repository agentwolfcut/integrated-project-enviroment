package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import dev.backendintegratedproject.services.PrimaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v3/users")
@CrossOrigin(origins = {"http://ip23kk3.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th"})
public class PrimaryUserController {

    @Autowired
    private PrimaryUserService primaryUserService;


    @GetMapping("")
    public ResponseEntity<List<PrimaryUser>> getAllUsers() {
        List<PrimaryUser> users = primaryUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/name/{username}")
    public ResponseEntity<PrimaryUser> getUserByUsername(@PathVariable String username) {
        PrimaryUser user = primaryUserService.findUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        return ResponseEntity.ok(user);
    }
}
