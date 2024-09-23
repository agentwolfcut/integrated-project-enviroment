package dev.backendintegratedproject.managements.repositories;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity , String> {

}
