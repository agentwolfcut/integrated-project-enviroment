package dev.backendintegratedproject.services;

import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
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
    @Transactional
    public TaskEntity addTask(TaskEntity task) {
        task.setCreatedOn(new Date());
        task.setUpdatedOn(new Date());
        return taskRepository.save(task);
    }
    public void deleteTask(Integer id) {
    TaskEntity task = taskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist"));
    taskRepository.delete(task);
}
    public TaskEntity editTask(Integer id, TaskEntity task) {
        TaskEntity existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {

            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setAssignees(task.getAssignees());
            existingTask.setStatus(task.getStatus());
            existingTask.setUpdatedOn(new Date());
            return taskRepository.save(existingTask);
        }
        return null;
    }

    public List<TaskEntity> getTasksByStatuses(List<StatusEntity> statusEntities, String[] sortBy, String[] direction) {
        if (sortBy != null && sortBy.length > 0) {
            List<Sort.Order> sortOrderList = new ArrayList<>();
            for (int i = 0; i < sortBy.length; i++) {
                sortOrderList.add(new Sort.Order(Sort.Direction.valueOf(direction[i].toUpperCase()), sortBy[i]));
            }
            return taskRepository.findAllByStatusIn(statusEntities, Sort.by(sortOrderList));
        } else {
            return taskRepository.findAllByStatusIn(statusEntities);
        }
    }

}
