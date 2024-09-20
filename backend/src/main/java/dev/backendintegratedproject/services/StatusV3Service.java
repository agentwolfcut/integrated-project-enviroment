package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.StatusDTO;
import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.entities.StatusV3Entity;
import dev.backendintegratedproject.managements.entities.TaskV3Entity;
import dev.backendintegratedproject.managements.repositories.BoardRepository;
import dev.backendintegratedproject.managements.repositories.StatusRepository;
import dev.backendintegratedproject.managements.repositories.TaskV3Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StatusV3Service {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TaskV3Repository taskV3Repository;
    @Autowired
    private ModelMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    public List<StatusDTO> findAllStatusesByBoardId(String boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
        List<StatusV3Entity> statuses = statusRepository.findByBoardId(board);
        return statuses.stream()
                .map(status -> mapper.map(status, StatusDTO.class))
                .toList();
    }

    public StatusDTO createNewStatus(StatusV3Entity status, String boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        if (status.getStatusName() == null || status.getStatusName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must not be null or empty");
        }

        boolean exists = statusRepository.existsByStatusNameAndBoardId(status.getStatusName(), String.valueOf(board));
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name must be unique within the board");
        }

        if (status.getStatusName().length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name size must be between 1 and 50");
        }

        if (status.getStatusDescription() != null && status.getStatusDescription().length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description size must be between 0 and 200");
        }

        status.setBoard(board);
        StatusV3Entity savedStatus = statusRepository.save(status);
        return mapper.map(savedStatus, StatusDTO.class);
    }

    @Transactional
    public StatusDTO updateStatus(String boardId, Integer statusId, StatusV3Entity updatedStatus) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        StatusV3Entity existingStatus = (StatusV3Entity) statusRepository.findByStatusIdAndBoardId(statusId, String.valueOf(board))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found in this board"));

        if (!existingStatus.getStatusName().equals(updatedStatus.getStatusName())) {
            boolean exists = statusRepository.existsByStatusNameAndBoardId(updatedStatus.getStatusName(), String.valueOf(board));
            if (exists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name must be unique within the board");
            }
        }

        existingStatus.setStatusName(updatedStatus.getStatusName());
        existingStatus.setStatusDescription(updatedStatus.getStatusDescription());

        StatusV3Entity savedStatus = statusRepository.save(existingStatus);
        return mapper.map(savedStatus, StatusDTO.class);
    }

    @Transactional
    public void deleteStatus(String boardId, Integer statusId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        StatusV3Entity status = (StatusV3Entity) statusRepository.findByStatusIdAndBoardId(statusId, String.valueOf(board))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found in this board"));

        if (status.getStatusName().equals("No Status") || status.getStatusName().equals("Done")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete 'No Status' or 'Done' status");
        }

        if (taskV3Repository.existsByStatusId(status.getStatusId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete Status as it is currently in use");
        }

        statusRepository.delete(status);
    }

    @Transactional
    public void deleteStatusAndTransferTasks(String boardId, Integer statusId, Integer newStatusId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        StatusV3Entity currentStatus = (StatusV3Entity) statusRepository.findByStatusIdAndBoardId(statusId, String.valueOf(board))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current status not found in this board"));

        StatusV3Entity newStatus = (StatusV3Entity) statusRepository.findByStatusIdAndBoardId(newStatusId, String.valueOf(board))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "New status not found in this board"));

        if (statusId.equals(newStatusId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination status must be different from current status");
        }

        List<TaskV3Entity> tasksWithCurrentStatus = taskV3Repository.findByStatusId(currentStatus);
        for (TaskV3Entity task : tasksWithCurrentStatus) {
            task.setStatusId(newStatus);
        }
        taskV3Repository.saveAll(tasksWithCurrentStatus);

        statusRepository.delete(currentStatus);
    }
}
