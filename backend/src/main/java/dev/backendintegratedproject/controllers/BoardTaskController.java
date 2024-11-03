package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.dtos.board.VisibilityDTO;
import dev.backendintegratedproject.dtos.board.CreateBoardDTO;
import dev.backendintegratedproject.dtos.task.CreateTaskDTO;
import dev.backendintegratedproject.dtos.task.DetailedTaskDTO;
import dev.backendintegratedproject.dtos.task.SimpleTaskDTO;
import dev.backendintegratedproject.dtos.task.UpdateTaskDTO;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.Task;
import dev.backendintegratedproject.services.TaskService;
import dev.backendintegratedproject.services.UserBoardService;
import dev.backendintegratedproject.services.BoardPermissionService;
import dev.backendintegratedproject.util.ListMapper;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v3/boards")
public class BoardTaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserBoardService userBoardService;

    @Autowired
    BoardPermissionService boardPermissionService;

    @Autowired
    ListMapper listMapper;

    @Autowired
    ModelMapper modelMapper; // เพิ่มการฉีด ModelMapper

    // Board API
    @GetMapping("")
    public ResponseEntity<List<Board>> getAllBoard(Authentication authentication) {
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        List<Board> boards = userBoardService.getBoardsByUserID(userDetails.getOid());
        return ResponseEntity.ok(boards);
    }

    @PostMapping("")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody CreateBoardDTO board, Authentication authentication) {
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board createdBoard = userBoardService.createBoardForUser(userDetails.getOid(), board.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @GetMapping("/{boardID}")
    public ResponseEntity<Board> getBoardDetails(@PathVariable String boardID, Authentication authentication) {
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }
        if (!board.isPublic() && !authenticationIsAuthorized(boardID, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
        }
        return ResponseEntity.ok(board);
    }

    // Similar logic for tasks
    @GetMapping("/{boardID}/tasks")
    public ResponseEntity<List<SimpleTaskDTO>> getTasks(@PathVariable String boardID,
                                                        @RequestParam(required = false, value = "filterStatuses") List<String> filterStatuses,
                                                        Authentication authentication) {
        verifyAccessOrThrow(boardID, authentication);
        List<Task> tasks = taskService.getTasksByStatuses(boardID, filterStatuses);
        return ResponseEntity.ok(listMapper.mapList(tasks, SimpleTaskDTO.class, modelMapper)); // แก้ไขการเรียกใช้ mapList
    }

    @GetMapping("/{boardID}/tasks/{taskID}")
    public ResponseEntity<DetailedTaskDTO> getTaskById(@PathVariable Integer taskID,
                                                       @PathVariable String boardID,
                                                       Authentication authentication) {
        verifyAccessOrThrow(boardID, authentication);
        return ResponseEntity.ok(modelMapper.map(taskService.getTaskById(taskID, boardID), DetailedTaskDTO.class));
    }

    @PostMapping("{boardID}/tasks")
    public ResponseEntity<DetailedTaskDTO> createTask(@PathVariable String boardID, @Valid @RequestBody CreateTaskDTO task, Authentication authentication) {
        verifyWritePermissionOrThrow(boardID, authentication);
        CreateTaskDTO taskTrim = taskService.trimTask(task);
        Task createdTask = taskService.createTask(taskTrim, boardID, getUserDetails(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdTask, DetailedTaskDTO.class));
    }

    @PutMapping("/{boardID}/tasks/{id}")
    public ResponseEntity<UpdateTaskDTO> updatedTask(@PathVariable Integer id,
                                                     @Valid @RequestBody(required = false) UpdateTaskDTO task,
                                                     @PathVariable String boardID,
                                                     Authentication authentication) {
        verifyWritePermissionOrThrow(boardID, authentication);
        UpdateTaskDTO taskTrim = taskService.trimTask(task);
        Task updatedTask = taskService.updateTask(id, taskTrim, boardID);
        return ResponseEntity.ok(modelMapper.map(updatedTask, UpdateTaskDTO.class));
    }

    @DeleteMapping("/{boardID}/tasks/{id}")
    public void deleteTask(@PathVariable Integer id, @PathVariable String boardID, Authentication authentication) {
        verifyWritePermissionOrThrow(boardID, authentication);
        taskService.deleteTask(id, boardID);
    }

    @PutMapping("/{boardID}/tasks/updateAll")
    public ResponseEntity<List<Task>> updatedAllTasks(@PathVariable String boardID,
                                                      @Valid @RequestBody List<Task> tasks,
                                                      Authentication authentication) {
        verifyWritePermissionOrThrow(boardID, authentication);
        List<Task> updatedTasks = taskService.saveAllTasks(tasks);
        return ResponseEntity.ok(updatedTasks);
    }

    @CrossOrigin("*")
    @PatchMapping("/{boardID}")
    public void changeVisibility(@PathVariable String boardID, @RequestBody(required = false) VisibilityDTO visibility, Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // Fetch board details and check ownership
        Board board = userBoardService.getBoardsDetail(boardID);
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to change this board's visibility.");
        }

        // Check if the request body (visibility) is provided and valid
        if (visibility == null || visibility.getVisibility() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body and visibility are required.");
        }

        // Set the new visibility
        userBoardService.setVisibility(boardID, visibility, userDetails.getOid());
    }

    private void verifyAccessOrThrow(String boardID, Authentication authentication) {
        if (!authenticationIsAuthorized(boardID, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
        }
    }

    private void verifyWritePermissionOrThrow(String boardID, Authentication authentication) {
        UserDetailsDTO userDetails = getUserDetails(authentication);
        BoardPermissionDTO permission = boardPermissionService.getPermission(userDetails.getOid(), boardID);
        if (!permission.getPermission().equals("WRITE")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to modify tasks in this board.");
        }
    }

    private boolean authenticationIsAuthorized(String boardID, Authentication authentication) {
        UserDetailsDTO userDetails = getUserDetails(authentication);
        Board board = userBoardService.getBoardsDetail(boardID);
        return boardPermissionService.canAccessBoard(userDetails, board);
    }

    private UserDetailsDTO getUserDetails(Authentication authentication) {
        return (UserDetailsDTO) authentication.getPrincipal();
    }
}
