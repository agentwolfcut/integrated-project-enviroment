package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.users.AccessTokenDTO;
import dev.backendintegratedproject.exceptions.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.backendintegratedproject.dtos.users.LoginUserDTO;
import dev.backendintegratedproject.dtos.users.TokenDTO;
import dev.backendintegratedproject.exceptions.LoginInvalidException;
import dev.backendintegratedproject.services.AuthenticationService;

@RestController
@RequestMapping("/v3")
@CrossOrigin(origins = {"http://ip23kk3.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th"})
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) throws LoginInvalidException {
        return ResponseEntity.ok(authenticationService.loginUser(loginUserDTO));
    }

    @PostMapping("/token")
    public ResponseEntity<AccessTokenDTO> refreshToken(HttpServletRequest request) throws InvalidTokenException {

        String refreshToken = request.getHeader("Authorization");

        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.replace("Bearer ", "");
        } else {
            throw new InvalidTokenException("Refresh token is missing or malformed");
        }

        return ResponseEntity.ok(authenticationService.refreshAccessToken(refreshToken));
    }



}
