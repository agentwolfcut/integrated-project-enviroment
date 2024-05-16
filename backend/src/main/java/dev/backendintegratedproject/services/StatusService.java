package dev.backendintegratedproject.services;
import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.StatusRepository;
import dev.backendintegratedproject.repositories.TaskRepository;
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
        return statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public StatusEntity addStatus(StatusEntity status) {
        if (status.getDescription() != null && status.getDescription().isEmpty()) {
            status.setDescription(null);
        }
        return statusRepository.save(status);
    }
    public StatusEntity getStatusByName(String name) {
        return statusRepository.findByName(name).orElse(null);
    }
//    public void editStatus(StatusEntity status) {
//        statusRepository.save(status);
//    }
    public StatusEntity editStatus(Integer id ,StatusEntity status){
        StatusEntity editStatus = statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(editStatus != null){
            // update
            editStatus.setName(status.getName());
            editStatus.setDescription(status.getDescription());

            return statusRepository.save(editStatus);
        }
        return null;
    }

    public void deleteStatus(Integer id) {
        StatusEntity task = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + id + " does not exist"));
        statusRepository.delete(task);

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
}

