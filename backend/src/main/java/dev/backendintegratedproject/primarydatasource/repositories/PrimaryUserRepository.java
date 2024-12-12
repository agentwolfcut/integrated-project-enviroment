package dev.backendintegratedproject.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PrimaryUserRepository extends JpaRepository<PrimaryUser, String> {

    Optional<PrimaryUser> findByUsername(String username);
}
