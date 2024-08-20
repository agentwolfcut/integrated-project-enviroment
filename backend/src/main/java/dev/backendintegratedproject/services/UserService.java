package dev.backendintegratedproject.services;

import dev.backendintegratedproject.userManage.UserEntity;
import dev.backendintegratedproject.userManage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public UserEntity getUserById(String oid) {
        return userRepository.findById(oid).orElse(null);
    }


}
