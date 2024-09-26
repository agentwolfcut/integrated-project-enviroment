package dev.backendintegratedproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import dev.backendintegratedproject.primarydatasource.repositories.PrimaryUserRepository;

@Service
public class PrimaryUserService {
    @Autowired
    PrimaryUserRepository primaryUserRepository;

    public Boolean checkUserExist(String userID){
        return primaryUserRepository.existsById(userID);
    }

    public void createUser(PrimaryUser user){
        primaryUserRepository.save(user);
    }
}
