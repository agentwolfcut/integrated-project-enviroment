package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.board.*;
import dev.backendintegratedproject.primarydatasource.entities.AccessRight;
import dev.backendintegratedproject.primarydatasource.entities.Collaborators;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import dev.backendintegratedproject.services.CollaboratorsService;
import dev.backendintegratedproject.util.JwtUtils;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.description.modifier.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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

import java.io.UnsupportedEncodingException;
import java.util.*;

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
    private CollaboratorsService collaboratorsService;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private BoardRepository repository;



    private String getOidFromHeader(String header) {
        if (header == null) return null;
        String token = header.substring(7);
        return jwtTokenUtil.getClaimValueFromToken(token, "oid");
    }

    private String getNameFromHeader(String header) {
        if (header == null) return null;
        String token = header.substring(7);
        return jwtTokenUtil.getClaimValueFromToken(token, "name");
    }

    public Board getBoard(String id) {
        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board ID cannot be null or empty.");
        }
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board doesn't exist"));
    }

    private Board permissionCheck(String authorizationHeader, String bid, String method, Boolean isCollabCanDoOperation) {
        String userOid = null;
        if (authorizationHeader != null) userOid = getOidFromHeader(authorizationHeader);
        Board board = userBoardService.getBoardsDetail(bid);

        return board;
    }

    private AccessRight oidCheck(Board board, String userOid, String method, Visibility visibility, Boolean isCollabCanDoOperation) {
        boolean isOwner = Objects.equals(board.getOwnerID(), userOid);
        Collaborators collab = isOwner ? null : collaboratorsService.getCollabOfBoard(board.getId(), userOid, false);

        boolean isWriteAccess = isOwner || (collab != null && collab.getAccessRight() == AccessRight.WRITE);
        boolean isCanDoOp = Objects.equals(method, "get") || isCollabCanDoOperation;

        if (!Objects.equals(method, "get") && !isWriteAccess && !Objects.equals(board.getId(), "kanbanbase")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission on this board");
        }
        if (!isOwner && !isCanDoOp && !Objects.equals(board.getId(), "kanbanbase")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission on this board");
        }

        return isOwner || (collab != null && collab.getAccessRight() == AccessRight.WRITE) ? AccessRight.WRITE : AccessRight.READ;
    }


    @GetMapping("/{id}/collabs")
    public ResponseEntity<Object> getCollab(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String id) {
        Board board = permissionCheck(authorizationHeader, id, "get", true);

        List<CollabOutputDTO> collabs = Collections.singletonList(collaboratorsService.mapOutputDTO((Collaborators) collaboratorsService.getAllCollabOfBoard(id)));
        return ResponseEntity.ok(collabs);
    }


    @GetMapping("/{id}/collabs/{UserOid}")
    public ResponseEntity<Object> getCollabById(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String id, @PathVariable String UserOid) {
        Board board = permissionCheck(authorizationHeader, id, "get", true);

        CollabOutputDTO collab = collaboratorsService.mapOutputDTO(collaboratorsService.getCollabOfBoard(id, UserOid, true));
        return ResponseEntity.ok(collab);
    }

    @PostMapping("/{id}/collabs")
    public ResponseEntity<Object> createCollab(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String id, @Valid @RequestBody(required = false) CollabCreateInputDTO input) throws MessagingException, UnsupportedEncodingException {
        Board board = permissionCheck(authorizationHeader, id, "post", false);

        CollabOutputDTO newCollab = collaboratorsService.mapOutputDTO(collaboratorsService.createNewCollab(board, input));
        return ResponseEntity.status(HttpStatus.CREATED).body(newCollab);
    }

    @PatchMapping("/{id}/collabs/{UserOid}")
    public ResponseEntity<Object> updateAccessRight(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String id, @PathVariable String UserOid, @RequestBody(required = false) AccessRightDTO input) throws MessagingException, UnsupportedEncodingException {
        Board board = permissionCheck(authorizationHeader, id, "patch", false);
        String userName = null;
        if (authorizationHeader != null) userName = getNameFromHeader(authorizationHeader);
        CollabOutputDTO collab = collaboratorsService.mapOutputDTO(collaboratorsService.updateCollab(id, UserOid, input));
        return ResponseEntity.ok(collab);
    }

    @DeleteMapping("/{id}/collabs/{UserOid}")
    public ResponseEntity<Object> deleteCollab(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String id, @PathVariable String UserOid) {
        String method = "delete";
        String oid = getOidFromHeader(authorizationHeader);
        if (Objects.equals(oid, UserOid)) method = "get";

        Board board = permissionCheck(authorizationHeader, id, method, false);

        CollabOutputDTO collab = collaboratorsService.mapOutputDTO(collaboratorsService.deleteCollab(id, UserOid));
        return ResponseEntity.ok().body(new HashMap<>());
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
