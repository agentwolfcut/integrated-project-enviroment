package dev.backendintegratedproject.services;

import dev.backendintegratedproject.managements.entities.StatusEntity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.managements.repositories.StatusRepository;
import dev.backendintegratedproject.managements.repositories.TaskRepository;
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



@Service
public class TaskService {

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskValidator taskValidator;


    public TaskEntity getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }


    @Transactional
    public TaskEntity addTask(TaskEntity task) {

        task.setCreatedOn(new Date());
        task.setUpdatedOn(new Date());
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Integer id) {
        taskValidator.validateTaskExists(id);
    }

    @Transactional
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




