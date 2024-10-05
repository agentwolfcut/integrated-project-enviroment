package dev.backendintegratedproject.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import dev.backendintegratedproject.dtos.task.CreateTaskDTO;
import dev.backendintegratedproject.dtos.task.UpdateTaskDTO;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Status;
import dev.backendintegratedproject.primarydatasource.entities.Task;
import dev.backendintegratedproject.primarydatasource.repositories.BoardPermissionRepository;
import dev.backendintegratedproject.primarydatasource.repositories.StatusRepository;
import dev.backendintegratedproject.primarydatasource.repositories.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    BoardPermissionRepository boardPermissionRepository;

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatuses(String boardID, List<String> statuses) {

        if (statuses == null || statuses.isEmpty()) {
            return taskRepository.findAllByBoardID(boardID);
        }
        return taskRepository.findAllByBoardIDAndStatusNameIn(boardID, statuses);
    }


    @Transactional(readOnly = true)
    public Task getTaskById(Integer id, String boardID) {
        Task task = taskRepository.findByIdAndBoardID(id, boardID);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist.");
        }
        return task;
    }

    @Transactional
    public Task createTask(CreateTaskDTO task, String boardID, UserDetailsDTO userDetailsDTO) {
        Status status = statusRepository.findByIdAndBoardID(task.getStatusId(), boardID);
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found");
        }
//        if (!boardPermissionRepository.checkBoardAccess(userDetailsDTO.getOid(), status.getBoardID()) || !boardID.equals(status.getBoardID())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The status does not exist");
//        }
        // Check if title, status is empty
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        if (!statusRepository.existsById(task.getStatusId())) {
            task.setStatusId(1);
        }
        Task newTask = modelMapper.map(task, Task.class);
        newTask.setBoardID(boardID);
        newTask.setStatus(statusRepository.findById(task.getStatusId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status " + task.getStatusId() + " does not exist.")));


        try {
            // Save task
            return taskRepository.save(newTask);
        } catch (DataIntegrityViolationException e) {
            // Handle specific constraint violation (e.g., unique constraint)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save task. Ensure data integrity.");
        } catch (Exception e) {
            // Handle any other unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Transactional
    public Task deleteTask(Integer id, String boardID) {
        // Check if task exists
        Task existingtask = taskRepository.findByIdAndBoardID(id, boardID);
        if (existingtask == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist.");
        }
        // Delete task
        try {
            taskRepository.delete(existingtask);
            return existingtask;
        } catch (Exception e) {
            // Handle any unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete task.", e);
        }
    }

    @Transactional
    public Task updateTask(Integer id, UpdateTaskDTO task, String boardID) {
        // Check if task exists
        Task existingTask = taskRepository.findByIdAndBoardID(id, boardID);
        if (existingTask == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist.");
        } //source for comparison
        Status newStatus = (statusRepository.findByIdAndBoardID(task.getStatusId(),boardID));
        if(newStatus == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status " + task.getStatusId() + " does not exist."); //Get the new status to upd
        }
        // Check updated values is valid
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        } else if (task.getStatusId() == null) {
            newStatus = (statusRepository.findById(1)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status 1 does not exist.")));
        }

        // Update title
        existingTask.setTitle(task.getTitle());
        // Update description
        existingTask.setDescription(task.getDescription());
        // Update assignees
        existingTask.setAssignees(task.getAssignees());
        // Update status
        existingTask.setStatus(newStatus);

        try {
            // Save updated task
            return taskRepository.save(existingTask);
        } catch (Exception e) {
            // Handle any unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update task.", e);
        }
    }

    @Transactional
    public List<Task> saveAllTasks(List<Task> tasks) {
        tasks.forEach(task -> {
            trimTask(task);
            if (task.getStatus() == null) {
                task.setStatus(statusRepository.findById(1)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status 1 does not exist.")));
            }

        });
        try {
            return taskRepository.saveAll(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save tasks.", e);
        }
    }

    public List<Task> getAllTaskWithStatus(Status status) {
        return taskRepository.findAllByStatus(status);
    }

    public Task trimTask(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        task.setAssignees(task.getAssignees() != null ? task.getAssignees().trim() : null);
        return task;
    }

    public CreateTaskDTO trimTask(CreateTaskDTO task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        task.setAssignees(task.getAssignees() != null ? task.getAssignees().trim() : null);
        return task;
    }

    public UpdateTaskDTO trimTask(UpdateTaskDTO task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        task.setAssignees(task.getAssignees() != null ? task.getAssignees().trim() : null);
        return task;
    }
}
