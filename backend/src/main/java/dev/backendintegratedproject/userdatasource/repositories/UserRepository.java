package dev.backendintegratedproject.userdatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.backendintegratedproject.userdatasource.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

}
