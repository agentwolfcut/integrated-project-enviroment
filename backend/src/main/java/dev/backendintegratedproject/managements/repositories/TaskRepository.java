package dev.backendintegratedproject.managements.repositories;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.entities.StatusEntity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.managements.entities.TaskV3Entity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findAllByStatus(StatusEntity statusEntity);
    List<TaskEntity> findAllByStatusIn(List<StatusEntity> statusEntities, Sort sort);

    boolean existsByStatus(StatusEntity status);

    List<TaskV3Entity> findByBoardId(BoardEntity board);

    Optional<Object> findByTaskIdAndBoardId(Integer taskId, BoardEntity board);


}

