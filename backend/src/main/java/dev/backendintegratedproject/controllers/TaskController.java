package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.GetTaskDTO;
import dev.backendintegratedproject.dtos.PutTaskDTO;
import dev.backendintegratedproject.dtos.TaskDTO;
import dev.backendintegratedproject.managements.entities.StatusEntity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.services.ListMapper;
import dev.backendintegratedproject.services.StatusService;
import dev.backendintegratedproject.services.TaskService;
import dev.backendintegratedproject.services.TaskValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/v2/tasks")
//@CrossOrigin(origins = "http://ip23kk3.sit.kmutt.ac.th")
@CrossOrigin(origins = {"http://localhost:5173",
			            "http://ip23kk3.sit.kmutt.ac.th",
                       	"http://intproj23.sit.kmutt.ac.th"})

public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private StatusService statusService;
    @Autowired
    private TaskValidator taskValidator;
    @GetMapping("")
    public ResponseEntity<Object> getTasks(
            @RequestParam(required = false) List<String> filterStatuses,
            @RequestParam(required = false,defaultValue = "") String[] sortBy,
            @RequestParam(required = false,defaultValue = "ASC") String[] direction) {

        List<TaskEntity> tasks = taskService.getAllTasks(filterStatuses, sortBy, direction);
        List<TaskDTO> taskDTOList = listMapper.mapList(tasks, TaskDTO.class, modelMapper);
        return ResponseEntity.ok(taskDTOList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Integer id) {
        TaskEntity task = taskService.getTaskById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Task id %d does not exist.", id));
        }
        GetTaskDTO getTaskDTO = modelMapper.map(task, GetTaskDTO.class);
        return new ResponseEntity<>(getTaskDTO, HttpStatus.OK);
    }
    // Old
    @PostMapping
    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskDTO taskDTO) {

        StatusEntity statusEntity = statusService.getStatusById(Integer.valueOf(taskDTO.getStatus()));
        TaskEntity taskEntity = modelMapper.map(taskDTO, TaskEntity.class);
        taskEntity.setStatus(statusEntity);
        taskValidator.validateTask(taskEntity);
        TaskEntity addedTask = taskService.addTask(taskEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> removeTask(@PathVariable Integer id) {
        TaskDTO taskDTO = taskService.deleteById(id);
        return ResponseEntity.ok(taskDTO);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> editTask(@Valid @PathVariable Integer id, @RequestBody TaskEntity task) {
//        taskValidator.validateTaskExists(id);
//        taskValidator.validateTask(task);
//        TaskEntity editedTask = taskService.editTask(id, task);
//        if (editedTask != null) {
//
//            StatusEntity statusEntity = statusService.getStatusById(editedTask.getStatus().getId());
//
//            PutTaskDTO responseDTO = modelMapper.map(editedTask, PutTaskDTO.class);
//            responseDTO.setStatus(statusEntity.getName());
//
//            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editTask(@PathVariable Integer id, @Valid @RequestBody PutTaskDTO putTaskDTO) {
        TaskEntity taskEntity = modelMapper.map(putTaskDTO, TaskEntity.class);
        TaskEntity editedTask = taskService.editTask(id, taskEntity);
        if (editedTask == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Task id %d does not exist.", id));
        }
        return ResponseEntity.ok(editedTask);
    }
}

