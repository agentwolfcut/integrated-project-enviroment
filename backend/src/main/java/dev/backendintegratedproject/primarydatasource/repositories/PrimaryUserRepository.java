package dev.backendintegratedproject.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;

public interface PrimaryUserRepository extends JpaRepository<PrimaryUser, String> {
}
