package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.StatusDTO;
import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.services.StatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static dev.backendintegratedproject.services.ListMapper.getInstance;

@RestController
@RequestMapping("/itb-kk/v2/statuses")
//@CrossOrigin(origins = "http://ip23kk3.sit.kmutt.ac.th")
@CrossOrigin(origins = "http://localhost:5173")
public class StatusController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Object> getAllStatus() {
        List<StatusEntity> statuses = statusService.getAllStatus();
        List<StatusDTO> statusDTOList = getInstance().mapList(statuses, StatusDTO.class, modelMapper);
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
}

//
//    @PostMapping
//    public ResponseEntity<String> addStatus(@RequestBody StatusDTO statusDTO) {
//        StatusEntity status = modelMapper.map(statusDTO, StatusEntity.class);
//        if (status.getStatusName() == null || status.getStatusName().isEmpty()) {
//            return ResponseEntity.badRequest().body("Status Name is required");
//        }
//        statusService.addStatus(status);
//        return ResponseEntity.ok("The status has been added");
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<String> editStatus(@PathVariable("id") Integer id, @RequestBody StatusDTO statusDTO) {
//        StatusEntity existingStatus = statusService.getStatusById(id);
//        if (existingStatus == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Status not found");
//        }
//        modelMapper.map(statusDTO, existingStatus);
//        statusService.editStatus(existingStatus);
//        return ResponseEntity.ok("The status has been updated");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteStatus(@PathVariable("id") Integer id) {
//        StatusEntity existingStatus = statusService.getStatusById(id);
//        if (existingStatus == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Status not found");
//        }
//        if (existingStatus.getStatusName().equals("No Status")) {
//            return ResponseEntity.badRequest().body("Cannot delete 'No Status'");
//        }
//        statusService.deleteStatus(id);
//        return ResponseEntity.ok("The status has been deleted");
//    }
//
//}
