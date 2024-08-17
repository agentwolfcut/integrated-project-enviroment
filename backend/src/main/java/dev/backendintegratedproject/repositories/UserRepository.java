package dev.backendintegratedproject.repositories;

import dev.backendintegratedproject.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {



}
