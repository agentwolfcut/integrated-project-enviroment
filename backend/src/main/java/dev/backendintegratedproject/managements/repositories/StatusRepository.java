package dev.backendintegratedproject.managements.repositories;
import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.entities.StatusEntity;
import dev.backendintegratedproject.managements.entities.StatusV3Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {
    Optional<StatusEntity> findByName(String name);
    boolean existsByName(String name);


    List<StatusV3Entity> findByBoardId(BoardEntity board);

    boolean existsByStatusNameAndBoardId(String statusName, String boardId);

    Optional<Object> findByStatusIdAndBoardId(Integer statusId, String boardId);
}


