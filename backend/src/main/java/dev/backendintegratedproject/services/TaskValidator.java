package dev.backendintegratedproject.services;

import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TaskValidator {
    @Autowired
    private TaskRepository taskRepository;

    public void validateTask(TaskEntity task) {
        StringBuilder errorMessage = new StringBuilder();

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            errorMessage.append("Title must not be null or empty. ");
        } else if (task.getTitle().length() > 100) {
            errorMessage.append("Title size must be between 0 and 100. ");
        }

        if (task.getDescription() != null && task.getDescription().length() > 500) {
            errorMessage.append("Description size must be between 0 and 500. ");
        }

        if (task.getAssignees() != null && task.getAssignees().length() > 30) {
            errorMessage.append("Assignees size must be between 0 and 30. ");
        }

    }

    public void validateTaskExists(Integer id) {
        // ค้นหา Task ด้วย ID ที่ระบุ
        TaskEntity task = taskRepository.findById(id).orElse(null);

        // หากไม่พบ Task กับ ID ที่ระบุ โยน ResponseStatusException
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist");
        }
    }
}
