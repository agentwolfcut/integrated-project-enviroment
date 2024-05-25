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


    @Transactional
    public TaskEntity addTask(TaskEntity task) {
//        validateTask(task);

        task.setCreatedOn(new Date());
        task.setUpdatedOn(new Date());
        return taskRepository.save(task);
    }
    @Transactional
    public void deleteTask(Integer id) {
    TaskEntity task = taskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist"));
    taskRepository.delete(task);
}   @Transactional
    public TaskEntity editTask(Integer id, TaskEntity task) {
//        validateTask(task);
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

//    private void validateTask(TaskEntity task) {
//        StringBuilder errorMessage = new StringBuilder();
//
//        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
//            errorMessage.append("Title must not be null or empty. ");
//        } else if (task.getTitle().length() > 100) {
//            errorMessage.append("Title size must be between 0 and 100. ");
//        }
//
//        if (task.getDescription() != null && task.getDescription().length() > 500) {
//            errorMessage.append("Description size must be between 0 and 500. ");
//        }
//
//        if (task.getAssignees() != null && task.getAssignees().length() > 30) {
//            errorMessage.append("Assignees size must be between 0 and 30. ");
//        }
//
//        if (errorMessage.length() > 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());
//        }
//    }


}
