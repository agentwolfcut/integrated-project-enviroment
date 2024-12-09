package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.services.CollaboratorsService;
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

    @Autowired
    private CollaboratorsService collaboratorsService;


    @GetMapping("/{boardID}/statuses")
    public ResponseEntity<List<Status>> getStatuses(@PathVariable String boardID, Authentication authentication) {
        // Fetch the board details
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // If the board is private, authentication and permission check are required
        if (!board.isPublic()) {
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
            }

            UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

            // Check if the user is the owner or a collaborator with READ access
            if (!board.getOwnerID().equals(userDetails.getOid())
                    && !collaboratorsService.hasReadAccess(userDetails.getOid(), boardID)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view statuses for this board.");
            }
        }

        // Fetch and return the statuses for the board
        List<Status> statuses = statusService.getStatusesForBoard(boardID);
        return ResponseEntity.ok(statuses);
    }





    @GetMapping("/{boardID}/statuses/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable String boardID,
                                                @PathVariable Integer id,
                                                Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board board = userBoardService.getBoardsDetail(boardID);

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasReadAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view statuses for this board.");
        }

        // ตรวจสอบว่ามีสถานะอยู่หรือไม่
        Status status = statusService.getOptionalStatusById(id, boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found."));

        return ResponseEntity.ok(status);
    }





    @PostMapping("/{boardID}/statuses")
    public ResponseEntity<Status> createStatus(@PathVariable String boardID,
                                               @Valid @RequestBody(required = false) CreateStatusDTO status,
                                               Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board board = userBoardService.getBoardsDetail(boardID);

        // ตรวจสอบว่าเป็นเจ้าของหรือมีสิทธิ์ WRITE
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add statuses to this board.");
        }

        // ตรวจสอบว่า Body มีข้อมูลที่ถูกต้อง
        if (status == null || status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required.");
        }

        // สร้างสถานะใหม่
        CreateStatusDTO statusTrim = statusService.trimStatus(status);
        Status createdStatus = statusService.createStatus(statusTrim, boardID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }



    @DeleteMapping("/{boardID}/statuses/{id}")
    public ResponseEntity<Object> deleteStatus(@PathVariable String boardID,
                                               @PathVariable Integer id,
                                               Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board board = userBoardService.getBoardsDetail(boardID);

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด: Owner หรือ WRITE access เท่านั้น
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete statuses for this board.");
        }

        // ตรวจสอบว่าสถานะมีอยู่หรือไม่
        Optional<Status> statusOptional = statusService.getOptionalStatusById(id, boardID);
        if (statusOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found.");
        }

        // ดำเนินการลบสถานะ
        statusService.deleteStatus(id, boardID);

        return ResponseEntity.ok().body(Map.of("message", "Status deleted successfully"));
    }



    @DeleteMapping("/{boardID}/statuses/{id}/{replaceId}")
    public void deleteStatus(
            @PathVariable Integer id,
            @PathVariable Integer replaceId,
            @PathVariable String boardID,
            Authentication authentication) {

        // ตรวจสอบว่าผู้ใช้ได้แนบ token มาหรือไม่
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        // ดึงข้อมูลบอร์ด
        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        String userOid = userDetails.getOid();

        // ตรวจสอบสิทธิ์ว่าเป็นเจ้าของหรือ collaborator ที่มี WRITE สิทธิ์
        if (!board.getOwnerID().equals(userOid) && !collaboratorsService.hasWriteAccess(userOid, boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete statuses for this board.");
        }

        // ตรวจสอบว่า status ที่ต้องการลบและ replace มีอยู่ในบอร์ดหรือไม่
        if (!statusService.existsStatus(id, boardID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status to delete not found.");
        }

        if (!statusService.existsStatus(replaceId, boardID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Replacement status not found.");
        }

        // ดำเนินการลบและแทนที่ status
        statusService.deleteAndReplaceStatus(id, replaceId, boardID);
    }


    @PutMapping("/{boardID}/statuses/{id}")
    public ResponseEntity<Status> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody(required = false) Status status,
            @PathVariable String boardID,
            Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board board = userBoardService.getBoardsDetail(boardID);

        // ตรวจสอบสิทธิ์การเข้าถึงบอร์ด: Owner หรือ WRITE access เท่านั้น
        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update statuses in this board.");
        }

        // ตรวจสอบว่าทรัพยากร (Status) มีอยู่
        Optional<Status> existingStatusOptional = statusService.getOptionalStatusById(id, boardID);
        if (existingStatusOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found.");
        }

        // ตรวจสอบว่า Body ถูกต้อง
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status data is required for update.");
        }

        // ตรวจสอบว่า Status มีการอัปเดตตามรูปแบบที่ถูกต้อง
        Status updatedStatus = statusService.updateStatus(id, status, boardID);

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
