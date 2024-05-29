package dev.backendintegratedproject.repositories;

import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.entities.TaskEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findAllByStatus(StatusEntity statusEntity);
    List<TaskEntity> findAllByStatusIn(List<StatusEntity> statusEntities, Sort sort);

    boolean existsByStatus(StatusEntity status);
}

