package dev.backendintegratedproject.repositories;

import dev.backendintegratedproject.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {


    UserEntity findByUsernameAndPassword(String username, String password);
}
