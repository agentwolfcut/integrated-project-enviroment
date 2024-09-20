package dev.backendintegratedproject.controllers;


import dev.backendintegratedproject.dtos.StatusDTO;
import dev.backendintegratedproject.managements.entities.StatusV3Entity;
import dev.backendintegratedproject.services.StatusV3Service;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/v3/boards/{boardId}/statuses")
@CrossOrigin(origins = {"http://localhost:5173", "http://ip23kk3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
public class StatusV3Controller {

    @Autowired
    private StatusV3Service statusService;

    @GetMapping("")
    public ResponseEntity<?> getAllStatuses(@PathVariable String boardId) {
        try {
            List<StatusDTO> statuses = statusService.findAllStatusesByBoardId(boardId);
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createStatus(@PathVariable String boardId, @RequestBody StatusV3Entity status) {
        try {
            StatusDTO createdStatus = statusService.createNewStatus(status, boardId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{statusId}")
    public ResponseEntity<?> updateStatus(@PathVariable String boardId, @PathVariable Integer statusId, @RequestBody StatusV3Entity status) {
        try {
            StatusDTO updatedStatus = statusService.updateStatus(boardId, statusId, status);
            return ResponseEntity.ok(updatedStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{statusId}")
    public ResponseEntity<?> deleteStatus(@PathVariable String boardId, @PathVariable Integer statusId) {
        try {
            statusService.deleteStatus(boardId, statusId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{statusId}/{newStatusId}")
    public ResponseEntity<?> deleteStatusAndTransferTasks(@PathVariable String boardId,
                                                          @PathVariable Integer statusId,
                                                          @PathVariable Integer newStatusId) {
        try {
            statusService.deleteStatusAndTransferTasks(boardId, statusId, newStatusId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}