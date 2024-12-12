package dev.backendintegratedproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import dev.backendintegratedproject.primarydatasource.repositories.PrimaryUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PrimaryUserService {
    @Autowired
    PrimaryUserRepository primaryUserRepository;


    public List<PrimaryUser> getAllUsers() {
        return primaryUserRepository.findAll();
    }

    public PrimaryUser findUserByUsername(String username) {
        Optional<PrimaryUser> user = primaryUserRepository.findByUsername(username);
        return user.orElse(null);
    }

    public Boolean checkUserExist(String userID){
        return primaryUserRepository.existsById(userID);
    }

    public void createUser(PrimaryUser user){
        primaryUserRepository.save(user);
    }
}
