package dev.backendintegratedproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.dtos.users.LoginUserDTO;
import dev.backendintegratedproject.dtos.users.TokenResponseDTO;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.exceptions.LoginInvalidException;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import dev.backendintegratedproject.primarydatasource.repositories.PrimaryUserRepository;
import dev.backendintegratedproject.util.JwtUtils;

@Service
public class AuthenticationService {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    PrimaryUserService primaryUserService;
    @Autowired
    PrimaryUserRepository primaryUserRepository;

    public TokenResponseDTO loginUser(LoginUserDTO loginUserDTO) throws LoginInvalidException {
        UserDetailsDTO user = null;
        try {
            user = userService.loadUserByUsername(loginUserDTO.getUsername());
            if (!primaryUserRepository.existsById(user.getOid())) {
                primaryUserService.createUser(new PrimaryUser(user.getOid(), user.getUsername()));
            }
        }catch (UsernameNotFoundException ex){
            throw new LoginInvalidException("Username or Password is incorrect.");
        }
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
        if (!argon2PasswordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
            throw new LoginInvalidException("Username or Password is incorrect.");
        }

        String accessToken = jwtUtils.generateToken(user);
        return new TokenResponseDTO(accessToken);
    }


}
