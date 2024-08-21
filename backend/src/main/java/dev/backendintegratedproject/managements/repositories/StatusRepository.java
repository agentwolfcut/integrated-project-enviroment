package dev.backendintegratedproject.managements.repositories;
import dev.backendintegratedproject.managements.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {
    Optional<StatusEntity> findByName(String name);
    boolean existsByName(String name);
//    boolean existsByNameAndIdNot(String name, Integer id);
}


