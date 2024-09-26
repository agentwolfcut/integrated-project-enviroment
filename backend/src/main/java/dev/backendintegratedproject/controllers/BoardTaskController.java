package dev.backendintegratedproject.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.dtos.board.CreateBoardDTO;
import dev.backendintegratedproject.dtos.task.CreateTaskDTO;
import dev.backendintegratedproject.dtos.task.DetailedTaskDTO;
import dev.backendintegratedproject.dtos.task.SimpleTaskDTO;
import dev.backendintegratedproject.dtos.task.UpdateTaskDTO;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;
import dev.backendintegratedproject.primarydatasource.entities.Task;
import dev.backendintegratedproject.services.BoardPermissionService;
import dev.backendintegratedproject.services.TaskService;
import dev.backendintegratedproject.services.UserBoardService;
import dev.backendintegratedproject.util.JwtUtils;
import dev.backendintegratedproject.util.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://ip23kk3.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th"})
public class BoardTaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    UserBoardService userBoardService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    BoardPermissionService boardPermissionService;

    // Board API
    @GetMapping("")
    public ResponseEntity<List<Board>> getAllBoards(Authentication authentication) {
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        return ResponseEntity.ok(listMapper.mapList(userBoardService.getBoardsByUserID(userDetails.getOid()), Board.class, modelMapper));
    }
    @PostMapping("")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody CreateBoardDTO board, Authentication authentication) {
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board createdBoard = userBoardService.createBoardForUser(userDetails.getOid(), board.getBoardName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }
    @GetMapping("/{boardID}")
    public ResponseEntity<Board> getBoardDetails(@PathVariable String boardID, Authentication authentication) {
        return ResponseEntity.ok((userBoardService.getBoardsDetail(boardID)));
    }
    @GetMapping("/{boardID}/permissions")
    public ResponseEntity<BoardPermissionDTO> getBoardPermission(Authentication authentication, @PathVariable String boardID) {
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        return ResponseEntity.ok(boardPermissionService.getPermission(userDetails.getOid(), boardID));
    }
    @PutMapping("/{boardID}/update")
    public ResponseEntity<Board> updateBoard(@PathVariable String boardID, @RequestBody Board board) {
        return ResponseEntity.ok(modelMapper.map(userBoardService.updateBoard(boardID, board), Board.class));
    }

    @DeleteMapping("/{boardID}/delete")
    public ResponseEntity<Board> deleteBoard(@PathVariable String boardID) {
        return ResponseEntity.ok(modelMapper.map(userBoardService.deleteBoard(boardID), Board.class));
    }

    // Task API
    @GetMapping("/{boardID}/tasks")
    public ResponseEntity<List<SimpleTaskDTO>> getTasks(@PathVariable String boardID, @RequestParam(required = false, value = "filterStatuses") List<String> filterStatuses) {
        return ResponseEntity.ok(listMapper.mapList(taskService.getTasksByStatuses(boardID, filterStatuses), SimpleTaskDTO.class, modelMapper));
    }

    @GetMapping("{boardID}/tasks/{taskID}")
    public ResponseEntity<DetailedTaskDTO> getTaskById(@PathVariable Integer taskID, @PathVariable String boardID) {
        return ResponseEntity.ok(modelMapper.map(taskService.getTaskById(taskID, boardID), DetailedTaskDTO.class));
    }

    @PostMapping("{boardID}/tasks")
    public ResponseEntity<CreateTaskDTO> createTask(@PathVariable String boardID, @Valid @RequestBody CreateTaskDTO task, Authentication authentication) {
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
        CreateTaskDTO taskTrim = taskService.trimTask(task);
        Task createdTask = taskService.createTask(taskTrim, boardID, userDetailsDTO);
        CreateTaskDTO taskDTO = modelMapper.map(createdTask, CreateTaskDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }

    @DeleteMapping("/{boardID}/tasks/{id}")
    public ResponseEntity<SimpleTaskDTO> deleteTask(@PathVariable Integer id, @PathVariable String boardID) {
        return ResponseEntity.ok(modelMapper.map(taskService.deleteTask(id, boardID), SimpleTaskDTO.class));
    }

    @PutMapping("/{boardID}/tasks/{id}")
    public ResponseEntity<UpdateTaskDTO> updatedTask(@PathVariable Integer id, @Valid @RequestBody UpdateTaskDTO task, @PathVariable String boardID) {
        UpdateTaskDTO taskTrim = taskService.trimTask(task);
        Task updatedTask = taskService.updateTask(id, taskTrim, boardID);
        UpdateTaskDTO taskDTO = modelMapper.map(updatedTask, UpdateTaskDTO.class);
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/{boardID}/tasks/updateAll")
    public List<Task> updatedAllTasks(@Valid @RequestBody List<Task> tasks) {
        return taskService.saveAllTasks(tasks);
    }
}
