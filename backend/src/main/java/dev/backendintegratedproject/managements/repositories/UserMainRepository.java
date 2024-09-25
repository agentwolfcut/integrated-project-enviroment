package dev.backendintegratedproject.managements.repositories;

import dev.backendintegratedproject.managements.entities.UserMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMainRepository extends JpaRepository<UserMainEntity, String> {
    Optional<Object> findByOid(String oid);
}
