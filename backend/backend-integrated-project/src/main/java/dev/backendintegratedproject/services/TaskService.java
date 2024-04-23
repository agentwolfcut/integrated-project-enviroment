package dev.backendintegratedproject.services;

import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskEntity getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }
}
