package dev.backendintegratedproject.services;
import dev.backendintegratedproject.managements.entities.StatusEntity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.managements.repositories.StatusRepository;
import dev.backendintegratedproject.managements.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<StatusEntity> getAllStatus() {
        return statusRepository.findAll();
    }

    public StatusEntity getStatusById(Integer id) {
        StatusEntity status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + id + " does not exist"));
        return status;
    }

    public StatusEntity addStatus(StatusEntity status) {
        validateStatus(status);

        if (status.getDescription() != null && status.getDescription().isEmpty()) {
            status.setDescription(null);
        }

        return statusRepository.save(status);
    }

    public StatusEntity editStatus(Integer id, StatusEntity status) {
        validateStatus(status);

        StatusEntity editStatus = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        editStatus.setName(status.getName());
        editStatus.setDescription(status.getDescription());

        return statusRepository.save(editStatus);
    }

    public void deleteStatus(Integer id) {
        StatusEntity status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + id + " does not exist"));

        if (StatusInUse(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete Status with ID " + id + " as it is currently in use.");
        }

        statusRepository.delete(status);
    }

    @Transactional
    public void deleteStatusAndTransferTasks(int id, int newStatusId) {
        StatusEntity currentStatus = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + id + " does not exist"));

        StatusEntity newStatus = statusRepository.findById(newStatusId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + newStatusId + " does not exist"));

        List<TaskEntity> tasksWithCurrentStatus = taskRepository.findAllByStatus(currentStatus);
        tasksWithCurrentStatus.forEach(task -> task.setStatus(newStatus));
        taskRepository.saveAll(tasksWithCurrentStatus);

        statusRepository.delete(currentStatus);
    }

    public boolean StatusInUse(StatusEntity status) {
        return taskRepository.existsByStatus(status);
    }

    private void validateStatus(StatusEntity status) {
        if (status.getName() == null || status.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must not be null or empty");
        }

        if (statusRepository.existsByName(status.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be unique");
        }

        if (status.getName().length() > 50 || (status.getDescription() != null && status.getDescription().length() > 200)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name size must be between 0 and 50, description size must be between 0 and 200");
        }

    }
}



