package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.GetTaskDTO;
import dev.backendintegratedproject.dtos.PutTaskDTO;
import dev.backendintegratedproject.dtos.TaskDTO;
import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.services.ListMapper;
import dev.backendintegratedproject.services.StatusService;
import dev.backendintegratedproject.services.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/v2/tasks")
//@CrossOrigin(origins = "http://ip23kk3.sit.kmutt.ac.th")
@CrossOrigin(origins = {"http://localhost:5173",
			            "http://ip23ft.sit.kmutt.ac.th",
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
    @GetMapping
    public ResponseEntity<Object> getTasks(@RequestParam(required = false) List<String> filterStatuses,
                                           @RequestParam(required = false) String[] sortBy,
                                           @RequestParam(required = false) String[] direction,
                                           @RequestParam(required = false) String taskAssignees) {

        if (taskAssignees != null) {
            return new ResponseEntity<>(Collections.singletonMap("error", "invalid filter parameter"), HttpStatus.BAD_REQUEST);
        }

        List<StatusEntity> statusEntities = new ArrayList<>();
        if (filterStatuses != null) {
            for (String status : filterStatuses) {
                StatusEntity statusEntity = statusService.getStatusByName(status);
                if (statusEntity != null) {
                    statusEntities.add(statusEntity);
                }
            }
        } else {
            statusEntities = statusService.getAllStatus();
        }

        List<TaskEntity> tasks = taskService.getTasksByStatuses(statusEntities, sortBy, direction);
        List<TaskDTO> taskDTOList = listMapper.mapList(tasks, TaskDTO.class, modelMapper);

        return new ResponseEntity<>(taskDTOList, HttpStatus.OK);
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
    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO) {
        StatusEntity statusEntity = statusService.getStatusById(Integer.valueOf(taskDTO.getStatus()));
        TaskEntity taskEntity = modelMapper.map(taskDTO, TaskEntity.class);
        taskEntity.setStatus(statusEntity);
        TaskEntity addedTask = taskService.addTask(taskEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>("The task has been deleted", HttpStatus.OK);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("An error has occurred, the task does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editTask(@PathVariable Integer id, @RequestBody TaskEntity task) {
        TaskEntity editedTask = taskService.editTask(id, task);
        if (editedTask != null) {

            StatusEntity statusEntity = statusService.getStatusById(editedTask.getStatus().getId());

            PutTaskDTO responseDTO = modelMapper.map(editedTask, PutTaskDTO.class);
            responseDTO.setStatus(statusEntity.getName());

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    //    @GetMapping
//    public ResponseEntity<Object> getTasks() {
//        List<TaskEntity> tasks = taskService.getAllTasks();
//        List<TaskDTO> taskDTOList = listMapper.mapList(tasks, TaskDTO.class,modelMapper);
//        return new ResponseEntity<>(taskDTOList, HttpStatus.OK);
//    }


    //@PostMapping
    //    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO) {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setTitle(taskDTO.getTitle());
//        taskEntity.setDescription(taskDTO.getDescription());
//        taskEntity.setAssignees(taskDTO.getAssignees());
//
//        // Fetch the status entity by name
//        StatusEntity statusEntity = statusService.getStatusByName(taskDTO.getStatus());
//        if (statusEntity == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status name");
//        }
//        taskEntity.setStatus(statusEntity);
//
//        TaskEntity addedTask = taskService.addTask(taskEntity);
//        return ResponseEntity.status(HttpStatus.CREATED).body(addedTask);
//    }

    // @PutMapping("/{id}")
//
//    public ResponseEntity<TaskEntity> editTask(@PathVariable Integer id, @RequestBody TaskEntity task) {
//        TaskEntity editedTask = taskService.editTask(id, task);
//        if (editedTask != null) {
//            return new ResponseEntity<>(editedTask, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

}

