package dev.backendintegratedproject.managements.repositories;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.entities.StatusV3Entity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.managements.entities.TaskV3Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskV3Repository extends JpaRepository<TaskV3Entity, Integer> {

    List<TaskV3Entity> findByStatusId(StatusV3Entity currentStatus);

    boolean existsByStatusId(Integer statusId);

    Optional<Object> findByTaskIdAndBoardId(Integer taskId, BoardEntity board);

    List<TaskV3Entity> findByBoardId(BoardEntity board);
}
