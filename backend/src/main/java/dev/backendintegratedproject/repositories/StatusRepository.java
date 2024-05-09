package dev.backendintegratedproject.repositories;
import dev.backendintegratedproject.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {
}


