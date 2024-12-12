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

        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        if (!board.isPublic()) {
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
            }

            UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

            if (!board.getOwnerID().equals(userDetails.getOid())
                    && !collaboratorsService.hasReadAccess(userDetails.getOid(), boardID)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view statuses for this board.");
            }
        }

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


        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasReadAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view statuses for this board.");
        }
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

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        Board board = userBoardService.getBoardsDetail(boardID);

        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add statuses to this board.");
        }

        if (status == null || status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required.");
        }

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

        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete statuses for this board.");
        }

        Optional<Status> statusOptional = statusService.getOptionalStatusById(id, boardID);
        if (statusOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found.");
        }

        statusService.deleteStatus(id, boardID);

        return ResponseEntity.ok().body(Map.of("message", "Status deleted successfully"));
    }

    @DeleteMapping("/{boardID}/statuses/{id}/{replaceId}")
    public void deleteStatus(
            @PathVariable Integer id,
            @PathVariable Integer replaceId,
            @PathVariable String boardID,
            Authentication authentication) {

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        Board board = userBoardService.getBoardsDetail(boardID);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found.");
        }

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        String userOid = userDetails.getOid();

        if (!board.getOwnerID().equals(userOid) && !collaboratorsService.hasWriteAccess(userOid, boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete statuses for this board.");
        }

        if (!statusService.existsStatus(id, boardID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status to delete not found.");
        }

        if (!statusService.existsStatus(replaceId, boardID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Replacement status not found.");
        }
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

        if (!board.getOwnerID().equals(userDetails.getOid())
                && !collaboratorsService.hasWriteAccess(userDetails.getOid(), boardID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update statuses in this board.");
        }

        Optional<Status> existingStatusOptional = statusService.getOptionalStatusById(id, boardID);
        if (existingStatusOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found.");
        }

        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status data is required for update.");
        }

        Status updatedStatus = statusService.updateStatus(id, status, boardID);

        return ResponseEntity.ok(updatedStatus);
    }


    @GetMapping("/{boardID}/statuses/usage/{id}")
    public Integer checkUsage(@PathVariable Integer id, @PathVariable String boardID, Authentication authentication){

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }

        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());
        return statusService.checkIsNotInUsed(id, boardID);
    }

    @GetMapping("/{boardID}/statuses/usage")
    public ResponseEntity<Map<Status, Integer>> checkAllUsage(@PathVariable String boardID, Authentication authentication){

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must provide a valid token to access this resource.");
        }
        Board board = userBoardService.getBoardsDetail(boardID);
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        userBoardService.checkBoardAccess(board, userDetails.getOid());
        return ResponseEntity.ok(statusService.getAllStatUsage(boardID));
    }
}
