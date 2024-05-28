package dev.backendintegratedproject.services;

import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.StatusRepository;
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
import java.util.Optional;

import static dev.backendintegratedproject.services.ListMapper.listMapper;

@Service
public class TaskService {

    @Autowired
    private StatusRepository statusRepository;
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
    }

    @Transactional
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

    public List<TaskEntity> getAllTasks(List<String> filterStatuses, String[] sortBy, String[] direction) {
        List<Sort.Order> sortOrderList = new ArrayList<>();


        if ((sortBy.length != 0 && !(sortBy[0].equals("status.name"))) || sortBy.length > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid filter parameter");
        }

        if (sortBy.length != 0) {
            for (int i = 0; i < sortBy.length; i++) {
                sortOrderList.add(new Sort.Order(
                        direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        sortBy[i]
                ));
            }
        } else {
            sortOrderList.add(new Sort.Order(Sort.Direction.ASC, "createdOn"));
        }

        if (filterStatuses != null && !filterStatuses.isEmpty()) {
            List<StatusEntity> statuses = new ArrayList<>();
            for (String name : filterStatuses) {
                Optional<StatusEntity> statusOptional = statusRepository.findByName(name);
                statusOptional.ifPresent(statuses::add);
            }
            return taskRepository.findAllByStatusIn(statuses, Sort.by(sortOrderList));
        }else {
            return taskRepository.findAll(Sort.by(sortOrderList));
        }
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



