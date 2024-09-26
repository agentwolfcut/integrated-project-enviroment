package dev.backendintegratedproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.backendintegratedproject.dtos.status.CreateStatusDTO;
import dev.backendintegratedproject.primarydatasource.entities.Status;
import dev.backendintegratedproject.primarydatasource.repositories.StatusRepository;
import dev.backendintegratedproject.primarydatasource.repositories.TaskRepository;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;



    public List<Status> getAllStatuses(String boardID) {
        return statusRepository.findAllByBoardID(boardID);
    }

    @Transactional(readOnly = true)
    public Status getStatusById(Integer id, String boardId) {
        Status status = statusRepository.findByIdAndBoardID(id,boardId);
        if(status == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found");
        }
        return status;
    }

    @Transactional
    public Status createStatus(CreateStatusDTO status, String boardID) {
        // Check if name is empty
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required.");
        }
        if (statusRepository.existsByNameAndBoardID(status.getName(), boardID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + status.getName() + ") already exists.");
        }
        try {
            Status newStatus = new Status();
            newStatus.setName(status.getName());
            newStatus.setDescription(status.getDescription());
            newStatus.setBoardID(boardID);
            return statusRepository.save(newStatus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save status.", e);
        }
    }

    @Transactional
    public void deleteStatus(Integer id, String boardId) {
        Status status = statusRepository.findByIdAndBoardID(id, boardId);
        if(status == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status to delete not found");
        }
        if (!taskService.getAllTaskWithStatus(status).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + status.getName() + " because it is in use !!!");
        }
        try {
            statusRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete status.", e);
        }
    }

    @Transactional
    public void deleteAndReplaceStatus(Integer deleteId, Integer replaceId, String boardId) {
        // Check if status exists
        if (deleteId.equals(replaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destination status for task transfer must be different from current status");
        }
        Status deleteStatus = statusRepository.findByIdAndBoardID(deleteId, boardId);
        Status replaceStatus = statusRepository.findByIdAndBoardID(replaceId, boardId);
        if(replaceStatus == null || deleteStatus == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found please check both status ID");
        }


        // fetch all tasks with deleteStatus




        try {
            // Replace all deleteStatus in tasks with replaceStatus
            taskRepository.transferStatusTasks(deleteStatus.getId(), replaceStatus.getId());
            // after replacing all deleteStatus in tasks, delete deleteStatus
            statusRepository.deleteById(deleteId);
        } catch (Exception e) {
            throw e;
        }



    }

    @Transactional
    public Status updateStatus(Integer id, Status updateStatus, String boardId) {
        // Check if status exists
        Status existingStatus = statusRepository.findByIdAndBoardID(id, boardId);
        if(existingStatus == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status to update not found");
        }
        // set existing status value

        // check if name is empty
        if (updateStatus.getName() == null || updateStatus.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required.");
        }
//        if (statusRepository.existsByName(updateStatus.getName()) && !updateStatus.getId().equals(existingStatus.getId())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + updateStatus.getName() + ") already exists.");
//        }
        if(statusRepository.findAllByName(updateStatus.getName()).stream().anyMatch(status -> !status.getId().equals(updateStatus.getId()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + updateStatus.getName() + ") already exists.");
        }
        // set updated values
        existingStatus.setName(updateStatus.getName());
        existingStatus.setDescription(updateStatus.getDescription());

        try {
            // save updated status
            return statusRepository.save(existingStatus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update status.", e);
        }
    }

    public Integer checkIsNotInUsed(Integer id, String boardId) {
        Status status = statusRepository.findByIdAndBoardID(id, boardId);
        if(status == null){
            new ResponseStatusException(HttpStatus.NOT_FOUND, "status " + id + " does not exist !!!");
        }
        return taskService.getAllTaskWithStatus(status).size();
    }

    public CreateStatusDTO trimStatus(CreateStatusDTO status) {
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required");
        }
        String trimmedName = status.getName().trim();
        String trimmedDescription = status.getDescription() != null ? status.getDescription().trim() : null;
        status.setName(trimmedName);
        status.setDescription(trimmedDescription);
        return status;
    }

    public Status trimStatusUpdate(Status status) {
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required");
        }
        String trimmedName = status.getName().trim();
        String trimmedDescription = status.getDescription() != null ? status.getDescription().trim() : null;
        status.setName(trimmedName);
        status.setDescription(trimmedDescription);
        return status;
    }

    public Map<Status, Integer> getAllStatUsage(String boardID) {
        Map usageMap = new HashMap();
        List<Status> statuses = getAllStatuses(boardID);
        statuses.forEach(status -> {
            usageMap.put(status.getId(), checkIsNotInUsed(status.getId(), boardID));
        });
        return usageMap;
    }
}
