package dev.backendintegratedproject.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.backendintegratedproject.dtos.status.CreateStatusDTO;
import dev.backendintegratedproject.primarydatasource.entities.Status;
import dev.backendintegratedproject.services.StatusService;
import dev.backendintegratedproject.util.ListMapper;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{boardID}/statuses")
    public List<Status> getAllStatuses(@PathVariable String boardID) {
        return statusService.getAllStatuses(boardID);
    }

    @GetMapping("/{boardID}/statuses/{id}")
    public Status getStatusById(@PathVariable Integer id, @PathVariable String boardID) {
        return statusService.getStatusById(id, boardID);
    }

    @PostMapping("/{boardID}/statuses")
    public ResponseEntity<Status> createStatus(@PathVariable String boardID, @Valid @RequestBody CreateStatusDTO status) {
        CreateStatusDTO statusTrim = statusService.trimStatus(status);
        Status createdStatus = statusService.createStatus(statusTrim, boardID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    @DeleteMapping("/{boardID}/statuses/{id}")
    public void deleteStatus(@PathVariable Integer id, @PathVariable String boardID) {
        statusService.deleteStatus(id, boardID);
    }

    @DeleteMapping("/{boardID}/statuses/{id}/{replaceId}")
    public void deleteStatus(@PathVariable Integer id, @PathVariable Integer replaceId, @PathVariable String boardID) {
        statusService.deleteAndReplaceStatus(id, replaceId, boardID);
    }

    @PutMapping("/{boardID}/statuses/{id}")
    public Status updateStatus(@PathVariable Integer id,@Valid @RequestBody Status status, @PathVariable String boardID) {
        Status statusTrim = statusService.trimStatusUpdate(status);
        return statusService.updateStatus(id, statusTrim, boardID);
    }

    @GetMapping("/{boardID}/statuses/usage/{id}")
    public Integer checkUsage(@PathVariable Integer id, @PathVariable String boardID){
        return statusService.checkIsNotInUsed(id, boardID);
    }

    @GetMapping("/{boardID}/statuses/usage")
    public ResponseEntity<Map<Status, Integer>> checkAllUsage(@PathVariable String boardID){
        return ResponseEntity.ok(statusService.getAllStatUsage(boardID));
    }
}
