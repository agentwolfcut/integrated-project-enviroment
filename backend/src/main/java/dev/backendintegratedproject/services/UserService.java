package dev.backendintegratedproject.services;

import dev.backendintegratedproject.userManage.UserEntity;
import dev.backendintegratedproject.userManage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

//    public boolean validateUser(String username, String password) {
//        UserEntity user = userRepository.findByUserName(username);
//        Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
//        if (user != null) {
//            return passwordEncoder.matches(password, user.getPassword());
//        }
//        return false;
//    }


//
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public List<UserEntity> getAllUsers() {
//        return userRepository.findAll();
//    }


}
