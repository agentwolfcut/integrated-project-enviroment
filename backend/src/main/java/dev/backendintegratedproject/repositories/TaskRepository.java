package dev.backendintegratedproject.repositories;

import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    public List<TaskEntity> findAllByStatus(StatusEntity statusEntity);
}

