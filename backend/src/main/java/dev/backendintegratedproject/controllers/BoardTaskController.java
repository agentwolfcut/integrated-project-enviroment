package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.board.CollaboratorRequest;
import dev.backendintegratedproject.dtos.board.VisibilityDTO;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;
import dev.backendintegratedproject.services.BoardPermissionService;
import dev.backendintegratedproject.services.SharedUserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
import dev.backendintegratedproject.util.ListMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v3/boards")

//@CrossOrigin(origins = {"http://ip23kk3.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th" })
public class BoardTaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ListMapper listMapper;

    @Autowired
    UserBoardService userBoardService;

    // Board API
    @Autowired
    private BoardPermissionService boardPermissionService;

    @Autowired
    private SharedUserService sharedUserService;


    @PostMapping("/{boardID}/collabs")
    public ResponseEntity<?> addCollaborator(@PathVariable String boardID, @RequestBody CollaboratorRequest request) {
        try {
            BoardPermission permission = boardPermissionService.addCollaborator(request.getUserID(), boardID, request.getPermission());
            return ResponseEntity.status(HttpStatus.CREATED).body(permission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardID}/collabs/{userID}")
    public ResponseEntity<?> removeCollaborator(@PathVariable String boardID, @PathVariable String userID) {
        try {
            boardPermissionService.removeCollaborator(userID, boardID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/{boardID}/collabs")
    public ResponseEntity<List<BoardPermission>> getCollaborators(@PathVariable String boardID) {
        List<BoardPermission> permissions = boardPermissionService.getCollaborators(boardID);
        return ResponseEntity.ok(permissions);
    }

    //patch
    @PatchMapping("/{boardID}/collabs/{userID}")
    public ResponseEntity<?> updateCollaborator(@PathVariable String boardID, @PathVariable String userID, @RequestBody CollaboratorRequest request) {
        try {
            BoardPermission permission = boardPermissionService.updateCollaborator(userID, boardID, request.getPermission());
            return ResponseEntity.ok(permission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
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

    // Allow public access for public boards, but enforce token check for private ones.
    @GetMapping("/{boardID}")
    public ResponseEntity<Board> getBoardDetails(@PathVariable String boardID, Authentication authentication) {
        // ตรวจสอบว่ามี Board นี้หรือไม่
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ถ้า Board เป็น public, อนุญาตให้เข้าถึงโดยไม่ต้องมี token
        if (board.isPublic()) {
            return ResponseEntity.ok(board);
        }

        // ตรวจสอบ token
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ถ้า Board เป็น private และผู้ใช้ไม่ได้เป็นเจ้าของ, ส่ง 403
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
        }

        return ResponseEntity.ok(board);
    }



    // Similar logic for tasks
    @GetMapping("/{boardID}/tasks")
    public ResponseEntity<List<SimpleTaskDTO>> getTasks(@PathVariable String boardID,
                                                        @RequestParam(required = false, value = "filterStatuses") List<String> filterStatuses,
                                                        Authentication authentication) {
        // ตรวจสอบว่ามี Board นี้หรือไม่
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ถ้า Board เป็น public, อนุญาตให้เข้าถึงโดยไม่ต้องมี token
        if (board.isPublic()) {
            List<Task> tasks = taskService.getTasksByStatuses(boardID, filterStatuses);
            return ResponseEntity.ok(listMapper.mapList(tasks, SimpleTaskDTO.class, modelMapper));
        }

        // ตรวจสอบ token
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ถ้า Board เป็น private และผู้ใช้ไม่ได้เป็นเจ้าของ, ส่ง 403
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board's tasks.");
        }

        List<Task> tasks = taskService.getTasksByStatuses(boardID, filterStatuses);
        return ResponseEntity.ok(listMapper.mapList(tasks, SimpleTaskDTO.class, modelMapper));
    }



    @GetMapping("/{boardID}/tasks/{taskID}")
    public ResponseEntity<DetailedTaskDTO> getTaskById(@PathVariable Integer taskID,
                                                       @PathVariable String boardID,
                                                       Authentication authentication) {
        // ตรวจสอบว่ามี Board นี้หรือไม่
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ถ้า Board เป็น public, อนุญาตให้เข้าถึงโดยไม่ต้องมี token
        if (board.isPublic()) {
            return ResponseEntity.ok(modelMapper.map(taskService.getTaskById(taskID, boardID), DetailedTaskDTO.class));
        }

        // ตรวจสอบ token
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ถ้า Board เป็น private และผู้ใช้ไม่ได้เป็นเจ้าของ, ส่ง 403
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this task.");
        }

        return ResponseEntity.ok(modelMapper.map(taskService.getTaskById(taskID, boardID), DetailedTaskDTO.class));
    }



    @CrossOrigin("*")
    @PatchMapping("/{boardID}")
    public void changeVisibility(@PathVariable String boardID, @RequestBody(required = false) VisibilityDTO visibility, Authentication authentication) {
        // Check if authentication is provided
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // Fetch board details and check ownership
        Board board = userBoardService.getBoardsDetail(boardID);
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            // If the user is not the owner, return 403
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to change this board's visibility.");
        }

        // Check if the request body (visibility) is provided and valid
        if (visibility == null || visibility.getVisibility() == null) {
            // Return 400 only when the body is missing or invalid, but the user is the owner
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body and visibility are required.");
        }

        // Set the new visibility
        userBoardService.setVisibility(boardID, visibility, userDetails.getOid());
    }



    @PostMapping("{boardID}/tasks")
    public ResponseEntity<DetailedTaskDTO> createTask(@PathVariable String boardID, @Valid @RequestBody CreateTaskDTO task, Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to create tasks in this board.");
        }

        // สร้าง task
        CreateTaskDTO taskTrim = taskService.trimTask(task);
        Task createdTask = taskService.createTask(taskTrim, boardID, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdTask, DetailedTaskDTO.class));
    }

    @PutMapping("/{boardID}/tasks/{id}")
    public ResponseEntity<UpdateTaskDTO> updatedTask(@PathVariable Integer id,
                                                     @Valid @RequestBody(required = false) UpdateTaskDTO task,
                                                     @PathVariable String boardID,
                                                     Authentication authentication) {
        // ตรวจสอบว่า token ถูกส่งมาหรือไม่
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update tasks in this board.");
        }

        // ตรวจสอบว่ามี Task อยู่ในบอร์ดนี้หรือไม่
        Optional<Task> existingTaskOptional = taskService.getOptionalTaskById(id, boardID);
        if (existingTaskOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found in this board.");
        }

        // อัปเดต task
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task data is required for update.");
        }

        UpdateTaskDTO taskTrim = taskService.trimTask(task);
        Task updatedTask = taskService.updateTask(id, taskTrim, boardID);

        return ResponseEntity.ok(modelMapper.map(updatedTask, UpdateTaskDTO.class));
    }


    @DeleteMapping("/{boardID}/tasks/{id}")
    public void deleteTask(@PathVariable Integer id, @PathVariable String boardID, Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete tasks in this board.");
        }

        taskService.deleteTask(id, boardID);
    }

    @PutMapping("/{boardID}/tasks/updateAll")
    public List<Task> updatedAllTasks(@Valid @RequestBody List<Task> tasks) {
        return taskService.saveAllTasks(tasks);
    }
}
