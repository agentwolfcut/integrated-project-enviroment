package dev.backendintegratedproject.services;
import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service

public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    public List<StatusEntity> getAllStatus() {
        return statusRepository.findAll();
    }

    public StatusEntity getStatusById(Integer id) {
        return statusRepository.findById(id).orElse(null);
    }

    public StatusEntity addStatus(StatusEntity status) {
        if (status.getDescription() != null && status.getDescription().isEmpty()) {
            status.setDescription(null);
        }
        return statusRepository.save(status);
    }
    public void editStatus(StatusEntity status) {
        statusRepository.save(status);
    }

    public void deleteStatus(Integer id) {
        StatusEntity task = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + id + " does not exist"));
        statusRepository.delete(task);
    }
}

