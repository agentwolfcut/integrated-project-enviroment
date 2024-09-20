package dev.backendintegratedproject.managements.repositories;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String> {
    List<BoardEntity> findAllByOwnerId(String ownerId);
}
