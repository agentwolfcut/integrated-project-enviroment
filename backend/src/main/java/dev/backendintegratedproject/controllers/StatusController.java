package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.StatusDTO;
import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.services.StatusService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/v2/statuses")
@CrossOrigin(origins = {"http://localhost:5173",
        "http://ip23ft.sit.kmutt.ac.th",
        "http://intproj23.sit.kmutt.ac.th"})
public class StatusController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Object> getAllStatus() {
        List<StatusEntity> statuses = statusService.getAllStatus();
        List<StatusDTO> statusDTOList = statuses.stream()
                .map(status -> modelMapper.map(status, StatusDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(statusDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStatusById(@PathVariable("id") Integer id) {
        StatusEntity status = statusService.getStatusById(id);
        if (status == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Status id %d does not exist.", id));
        StatusDTO statusDTO = modelMapper.map(status, StatusDTO.class);
        return new ResponseEntity<>(statusDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addStatus(@Valid @RequestBody StatusDTO statusDTO) {
        StatusEntity status = modelMapper.map(statusDTO, StatusEntity.class);
        StatusEntity addedStatus = statusService.addStatus(status);
        StatusDTO addedStatusDTO = modelMapper.map(addedStatus, StatusDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedStatusDTO);

    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> editStatus(@Valid @PathVariable Integer id, @RequestBody StatusEntity status) {
        if ("No Status".equals(statusService.getStatusById(id).getName()) || "Done".equals(statusService.getStatusById(id).getName())){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Status and Done Cannot be Edited");}
        StatusEntity editedStatus = statusService.editStatus(id,status);
        return ResponseEntity.ok(editedStatus);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStatus(@PathVariable("id") Integer id) {
        if ("No Status".equals(statusService.getStatusById(id).getName()) || "Done".equals(statusService.getStatusById(id).getName())){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Status and Done Cannot be deleted");}
        statusService.deleteStatus(id);
        return ResponseEntity.ok().body(new HashMap<>());
    }

    @DeleteMapping("/{id}/{newId}")
    public ResponseEntity<Object> deleteStatusAndTransferTasks(@PathVariable int id, @PathVariable int newId) {
        statusService.deleteStatusAndTransferTasks(id, newId);
        return ResponseEntity.ok("{}");
    }
}