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
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;

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

    private Board permissionCheck(String authorizationHeader, String bid, String method, Boolean isCollabCanDoOperation) {
        String userOid = null;
        if (authorizationHeader != null) userOid = getOidFromHeader(authorizationHeader);
        Board board = userBoardService.getBoardsDetail(bid);
        if ((!Objects.equals(method, "get") && board.getVisibility().equals(Visibility.PUBLIC) && userOid != null) || board.getVisibility().equals(Visibility.PRIVATE)) {
            oidCheck(board, userOid, method, board.getVisibility(), isCollabCanDoOperation);
        }
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

    //get all boards
    @GetMapping("")
    public ResponseEntity<Object> getAllBoards(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Extract user OID from the Authorization header
        String userOid = getOidFromHeader(authorizationHeader);

        // Fetch boards owned by the user
        List<Board> ownedBoards = userBoardService.getBoardsByOwnerID(userOid);
        List<BoardDTO> ownedBoardDTOs = ownedBoards.stream()
                .map(board -> {
                    BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
                    boardDTO.setCollaborators(new ArrayList<>()); // Ensure empty collaborators for owned boards
                    return boardDTO;
                })
                .toList();

        // Fetch boards where the user is a collaborator
        List<Collaborators> collabs = collaboratorsService.getAllCollabByOid(userOid);
        List<Map<String, Object>> collabsBoard = collabs.stream()
                .map(collab -> {
                    // Get board details using boardId from Collaborators
                    Board board = userBoardService.getBoardsDetail(collab.getBoardID());

                    // Fetch owner details using ownerId from Board
                    String ownerId = board.getOwnerID();
                    UserDetailsDTO ownerDetails = userBoardService.getUserDetailsById(ownerId);

                    // Map collaborator details along with board and owner information
                    Map<String, Object> collabDetails = new HashMap<>();
                    collabDetails.put("oid", ownerId); // Owner's OID
                    collabDetails.put("boardId", collab.getBoardID());
                    collabDetails.put("accessRight", collab.getAccessRight());
                    collabDetails.put("boardname", board.getName()); // Board name
                    collabDetails.put("name", ownerDetails.getName()); // Owner's name
                    collabDetails.put("email", ownerDetails.getEmail()); // Owner's email
                    return collabDetails;
                })
                .toList();

        // Combine both owned boards and collaborated boards into the response
        Map<String, Object> response = new HashMap<>();
        response.put("ownedBoards", ownedBoardDTOs);
        response.put("collabBoards", collabsBoard);

        return ResponseEntity.ok(response);
    }




    @GetMapping("/{id}/collabs")
    public ResponseEntity<Map<String, Object>> getCollab(@PathVariable String id) {

        // ดึงข้อมูลบอร์ด (ไม่ต้องตรวจสอบ Permission)
        Board board = userBoardService.getBoardsDetail(id);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ดึง List<Collaborators>
        List<Collaborators> collaborators = collaboratorsService.getAllCollabOfBoard(id);

        // แปลง List<Collaborators> เป็น List<CollabOutputDTO> และเพิ่ม boardId
        List<Map<String, Object>> collabs = collaborators.stream()
                .map(collaborator -> {
                    CollabOutputDTO dto = collaboratorsService.mapOutputDTO(collaborator);
                    Map<String, Object> collabWithBoardId = new HashMap<>();
                    collabWithBoardId.put("boardId", id);
                    collabWithBoardId.put("collaborator", dto);
                    return collabWithBoardId;
                })
                .toList();

        // ใส่ข้อมูลใน Map เพื่อแสดงผล
        Map<String, Object> response = new HashMap<>();
        response.put("boardId", id);
        response.put("collaborators", collabs);

        return ResponseEntity.ok(response);
    }




    @GetMapping("/{id}/collabs/{UserOid}")
    public ResponseEntity<Object> getCollabById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String id,
            @PathVariable String UserOid) {
        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        permissionCheck(authorizationHeader, id, "get", true);

        // ตรวจสอบว่ามี collaborator สำหรับ board นี้หรือไม่
        return collaboratorsService.getCollabByBoardIdAndUserOid(id, UserOid)
                .map(collaboratorsService::mapOutputDTO)
                .map((Function<? super CollabOutputDTO, ? extends ResponseEntity<Object>>) ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator not found on this board."));
    }


    @PostMapping("/{id}/collabs")
    public ResponseEntity<Object> createCollab(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @PathVariable String id,
            @Valid @RequestBody(required = false) CollabCreateInputDTO input) throws MessagingException, UnsupportedEncodingException {

        // ตรวจสอบสิทธิ์ของผู้ใช้ (ต้องการสิทธิ์ WRITE หรือเป็นเจ้าของ)
        Board board = permissionCheck(authorizationHeader, id, "post", false);

        // ตรวจสอบว่า request body ถูกต้อง
        if (input == null || input.getAccessRight() == null || input.getAccessRight().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body or required fields are missing or invalid.");
        }

        // ตรวจสอบสิทธิ์ (เฉพาะเจ้าของหรือผู้ที่มี WRITE เท่านั้น)
        String userOid = getOidFromHeader(authorizationHeader);
        if (!board.getOwnerID().equals(userOid) && !collaboratorsService.hasWriteAccess(userOid, board.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to perform this action.");
        }

        // ดำเนินการเพิ่ม collaborator
        CollabOutputDTO newCollab = collaboratorsService.mapOutputDTO(collaboratorsService.createNewCollab(board, input));
        return ResponseEntity.status(HttpStatus.CREATED).body(newCollab);
    }


    @PatchMapping("/{id}/collabs/{UserOid}")
    public ResponseEntity<Object> updateAccessRight(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String id,
            @PathVariable String UserOid,
            @Valid @RequestBody(required = false) UpdateAccessRightDTO input) throws MessagingException, UnsupportedEncodingException {

        // Step 1: Check if the Authorization header is missing
        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is required.");
        }

        // Step 2: Permission check before validating input
        Board board = permissionCheck(authorizationHeader, id, "patch", false);

        String userName = null;
        if (authorizationHeader != null) {
            userName = getNameFromHeader(authorizationHeader);
        }

        // Step 3: Validate input after permission check
        if (input == null || input.getAccessRight() == null || input.getAccessRight().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AccessRight is required and cannot be null or empty.");
        }

        // Step 4: Update collaborator
        CollabOutputDTO collab = collaboratorsService.mapOutputDTO(
                collaboratorsService.updateCollab(id, UserOid, input)
        );

        return ResponseEntity.ok(collab);
    }


    @DeleteMapping("/{id}/collabs/{userOid}")
    public ResponseEntity<Object> deleteCollab(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String id,
            @PathVariable String userOid) {

        // ตรวจสอบว่า Authorization header มีอยู่หรือไม่
        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is required.");
        }

        // ดึง OID ของผู้ร้องขอ
        String requesterOid = getOidFromHeader(authorizationHeader);

        // ตรวจสอบว่า board มีอยู่จริง
        Board board = userBoardService.getBoardsDetail(id);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ตรวจสอบว่า collaborator ที่จะถูกลบมีอยู่ในบอร์ดหรือไม่
        Optional<Collaborators> collabOptional = collaboratorsService.getOptionalCollabOfBoard(id, userOid);
        if (collabOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator not found.");
        }

        // กรณีลบตัวเองออกจากบอร์ด (leave)
        if (requesterOid.equals(userOid)) {
            collaboratorsService.deleteCollab(id, userOid);
            return ResponseEntity.ok().body(new HashMap<>());
        }

        // ตรวจสอบสิทธิ์สำหรับการลบ collaborator คนอื่น
        boolean isOwner = board.getOwnerID().equals(requesterOid);
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the board owner can remove a collaborator.");
        }

        // ดำเนินการลบ collaborator คนอื่นโดยเจ้าของบอร์ด
        collaboratorsService.deleteCollab(id, userOid);

        // ส่งผลลัพธ์สำเร็จกลับไป
        return ResponseEntity.ok().body(new HashMap<>());
    }





    @PostMapping("")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody CreateBoardDTO board, Authentication authentication) {
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board createdBoard = userBoardService.createBoardForUser(userDetails.getOid(), board.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @GetMapping("/{boardID}")
    public ResponseEntity<Map<String, Object>> getBoardDetails(
            @PathVariable String boardID,
            Authentication authentication) {
        // Fetch board details
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // Check if the board is public
        if (!board.isPublic()) {
            // If the board is private, authentication is required
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
            }

            // Extract the user details
            UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
            String userOid = userDetails.getOid();

            // Check if the user is the owner of the board
            if (!board.getOwnerID().equals(userOid)) {
                // If not the owner, check if the user is a collaborator
                boolean isCollaborator = collaboratorsService.hasReadAccess(userOid, boardID);
                if (!isCollaborator) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
                }
            }
        }

        // Fetch owner details
        String ownerID = board.getOwnerID();
        UserDetailsDTO ownerDetails = userBoardService.getUserDetailsById(ownerID);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("id", board.getId());
        response.put("name", board.getName());
        response.put("ownerID", ownerID);
        response.put("owner", Map.of(
                "oid", ownerDetails.getOid(),
                "username", ownerDetails.getName(),
                "email", ownerDetails.getEmail() // Include email
        ));
        response.put("public", board.isPublic());
        response.put("visibility", board.getVisibility().name());

        return ResponseEntity.ok(response);
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

        // หาก Board เป็น Public
        if (board.isPublic()) {
            List<Task> tasks = taskService.getTasksByStatuses(boardID, filterStatuses);
            return ResponseEntity.ok(listMapper.mapList(tasks, SimpleTaskDTO.class, modelMapper));
        }

        // หาก Board เป็น Private ให้ตรวจสอบ token
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ตรวจสอบว่าเป็นเจ้าของหรือ Collaborator ที่มีสิทธิ์หรือไม่
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasReadAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
        }

        List<Task> tasks = taskService.getTasksByStatuses(boardID, filterStatuses);
        return ResponseEntity.ok(listMapper.mapList(tasks, SimpleTaskDTO.class, modelMapper));
    }




    @GetMapping("/{boardID}/tasks/{taskID}")
    public ResponseEntity<DetailedTaskDTO> getTaskById(@PathVariable Integer taskID,
                                                       @PathVariable String boardID,
                                                       Authentication authentication) {
        // ตรวจสอบว่า Board มีอยู่จริง
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึง
        if (!board.isPublic()) {
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
            }

            UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
            if (!board.getOwnerID().equals(userDetails.getOid())
                    && !collaboratorsService.hasReadAccess(userDetails.getOid(), boardID)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
            }
        }

        // ตรวจสอบว่ามี Task ใน Board นี้หรือไม่
        Optional<Task> taskOptional = taskService.getOptionalTaskById(taskID, boardID);
        if (taskOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }

        Task task = taskOptional.get();
        return ResponseEntity.ok(modelMapper.map(task, DetailedTaskDTO.class));
    }




    @CrossOrigin("*")
    @PatchMapping("/{boardID}")
    public ResponseEntity<VisibilityDTO> changeVisibility(
            @PathVariable String boardID,
            @RequestBody(required = false) VisibilityDTO visibility,
            Authentication authentication) {
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

        // Return the updated visibility in the response
        return ResponseEntity.ok(visibility);
    }




    @PostMapping("{boardID}/tasks")
    public ResponseEntity<DetailedTaskDTO> createTask(@PathVariable String boardID,
                                                      @Valid @RequestBody(required = false) CreateTaskDTO task,
                                                      Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board board = userBoardService.getBoardsDetail(boardID);

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to create tasks in this board.");
        }

        // ตรวจสอบ Body
        if (task == null || task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task title is required.");
        }

        // สร้าง Task
        CreateTaskDTO taskTrim = taskService.trimTask(task);
        Task createdTask = taskService.createTask(taskTrim, boardID, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdTask, DetailedTaskDTO.class));
    }



    @PutMapping("/{boardID}/tasks/{id}")
    public ResponseEntity<?> updatedTask(@PathVariable Integer id,
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
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update tasks in this board.");
        }

        // ตรวจสอบว่ามี Task อยู่ในบอร์ดนี้หรือไม่
        Optional<Task> existingTaskOptional = taskService.getOptionalTaskById(id, boardID);
        if (existingTaskOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID " + id + " not found in this board.");
        }

        // ตรวจสอบว่า Body ถูกต้อง
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task data is required for update.");
        }

        // อัปเดต task
        try {
            UpdateTaskDTO taskTrim = taskService.trimTask(task);
            Task updatedTask = taskService.updateTask(id, taskTrim, boardID);
            return ResponseEntity.ok(modelMapper.map(updatedTask, UpdateTaskDTO.class));
        } catch (ResponseStatusException e) {
            // คืนค่าข้อผิดพลาดที่กำหนดเองจาก service layer
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // จัดการข้อผิดพลาดทั่วไป
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }



    @DeleteMapping("/{boardID}/tasks/{id}")
    public void deleteTask(@PathVariable Integer id, @PathVariable String boardID, Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        Board board = userBoardService.getBoardsDetail(boardID);

        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete tasks in this board.");
        }

        // เช็คว่าทรัพยากร (Task) มีอยู่
        Optional<Task> taskOptional = taskService.getOptionalTaskById(id, boardID);
        if (taskOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }

        taskService.deleteTask(id, boardID);
    }


    @PutMapping("/{boardID}/tasks/updateAll")
    public List<Task> updatedAllTasks(@Valid @RequestBody List<Task> tasks) {
        return taskService.saveAllTasks(tasks);
    }
}
