package dev.backendintegratedproject.services;

import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskRepository taskRepository;

    public TaskEntity getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }
    public TaskEntity addTask(TaskEntity task) {
        task.saveTransientStatusEntity(entityManager);
        task.setCreatedOn(new Date());
        task.setUpdatedOn(new Date());
        return taskRepository.save(task);
    }

//    public void deleteTask(Integer id) {
//        TaskEntity task = taskRepository.findById(id)
//                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist"));
//        taskRepository.delete(task);
//    }
    public void deleteTask(Integer id) {
    TaskEntity task = taskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist"));
    taskRepository.delete(task);
}

    public TaskEntity editTask(Integer id, TaskEntity task) {
        TaskEntity existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            // Update existing task's fields except for createdOn and updatedOn
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setAssignees(task.getAssignees());
            existingTask.setStatus(task.getStatus());
            existingTask.setUpdatedOn(new Date());
            task.saveTransientStatusEntity(entityManager);
            return taskRepository.save(existingTask);
        }
        return null; // Or throw an exception if required
    }

}
