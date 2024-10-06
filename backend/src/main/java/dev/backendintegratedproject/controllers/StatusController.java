package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.task.SimpleTaskDTO;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.Task;
import dev.backendintegratedproject.services.UserBoardService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import dev.backendintegratedproject.dtos.status.CreateStatusDTO;
import dev.backendintegratedproject.primarydatasource.entities.Status;
import dev.backendintegratedproject.services.StatusService;
import dev.backendintegratedproject.util.ListMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://ip23kk3.sit.kmutt.ac.th:80", "http://localhost:5173" ,"http://intproj23.sit.kmutt.ac.th"})
public class StatusController {
    @Autowired
    StatusService statusService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    UserBoardService userBoardService;


    @GetMapping("/{boardID}/statuses")
    public ResponseEntity<List<Status>> getAllStatuses(@PathVariable String boardID, Authentication authentication) {
        // ตรวจสอบว่ามี Board นี้หรือไม่
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ถ้า Board เป็น public, อนุญาตให้เข้าถึงได้โดยไม่ต้องมี token
        if (board.isPublic()) {
            List<Status> statuses = statusService.getAllStatuses(boardID);
            return ResponseEntity.ok(statuses);
        }

        // ตรวจสอบ token
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board's statuses.");
        }

        List<Status> statuses = statusService.getAllStatuses(boardID);
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/{boardID}/statuses/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable Integer id,
                                                @PathVariable String boardID,
                                                Authentication authentication) {
        // ตรวจสอบว่ามี Board นี้หรือไม่
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ถ้า Board เป็น public, อนุญาตให้เข้าถึงโดยไม่ต้องมี token
        if (board.isPublic()) {
            Status status = statusService.getStatusById(id, boardID);
            if (status == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found.");
            }
            return ResponseEntity.ok(status);
        }

        // ตรวจสอบ token
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        if (!board.getOwnerID().equals(userDetails.getOid())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board's statuses.");
        }

        // ตรวจสอบว่า Status มีอยู่ในบอร์ดหรือไม่
        Status status = statusService.getStatusById(id, boardID);
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found.");
        }

        return ResponseEntity.ok(status);
    }





    @PostMapping("/{boardID}/statuses")
    public ResponseEntity<Status> createStatus(@PathVariable String boardID, @Valid @RequestBody CreateStatusDTO status, Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());
        CreateStatusDTO statusTrim = statusService.trimStatus(status);
        Status createdStatus = statusService.createStatus(statusTrim, boardID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    @DeleteMapping("/{boardID}/statuses/{id}")
    public void deleteStatus(@PathVariable Integer id, @PathVariable String boardID, Authentication authentication) {
        // ตรวจสอบว่าผู้ใช้ได้แนบ token มาหรือไม่
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());

        statusService.deleteStatus(id, boardID);
    }

    @DeleteMapping("/{boardID}/statuses/{id}/{replaceId}")
    public void deleteStatus(@PathVariable Integer id, @PathVariable Integer replaceId, @PathVariable String boardID, Authentication authentication) {

        // ตรวจสอบว่าผู้ใช้ได้แนบ token มาหรือไม่
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());

        statusService.deleteAndReplaceStatus(id, replaceId, boardID);
    }

    @PutMapping("/{boardID}/statuses/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Integer id,
                                               @Valid @RequestBody(required = false) Status status,
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update statuses in this board.");
        }

        // ตรวจสอบว่ามี Status อยู่ในบอร์ดนี้หรือไม่
        Optional<Status> existingStatusOptional = statusService.getOptionalStatusById(id, boardID);
        if (existingStatusOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status with ID " + id + " not found in this board.");
        }

        // ตรวจสอบว่ามีข้อมูล Status ส่งมาเพื่ออัปเดตหรือไม่
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status data is required for update.");
        }

        // อัปเดต Status
        Status statusTrim = statusService.trimStatusUpdate(status);
        Status updatedStatus = statusService.updateStatus(id, statusTrim, boardID);

        return ResponseEntity.ok(updatedStatus);
    }



    @GetMapping("/{boardID}/statuses/usage/{id}")
    public Integer checkUsage(@PathVariable Integer id, @PathVariable String boardID, Authentication authentication){

        // ตรวจสอบว่าผู้ใช้ได้แนบ token มาหรือไม่
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());
        return statusService.checkIsNotInUsed(id, boardID);
    }

    @GetMapping("/{boardID}/statuses/usage")
    public ResponseEntity<Map<Status, Integer>> checkAllUsage(@PathVariable String boardID, Authentication authentication){

        // ตรวจสอบว่าผู้ใช้ได้แนบ token มาหรือไม่
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());
        return ResponseEntity.ok(statusService.getAllStatUsage(boardID));
    }
}
