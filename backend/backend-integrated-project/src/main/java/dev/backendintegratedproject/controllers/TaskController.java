package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itb-kk/v1/tasks")
 // @CrossOrigin(origins = "http://ip23kk3.sit.kmutt.ac.th:8080")
@CrossOrigin(origins = "http://localhost:5173")

public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskEntity>> getTasks() {
        List<TaskEntity> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Integer id) {
        TaskEntity task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, task == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
    // for add  edit delete
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public TaskEntity addTask(@RequestBody TaskEntity task) {
//        return taskService.addTask(task);
//    }
//
//    @PutMapping("/{id}")
//    public TaskEntity editTask(@PathVariable Long id, @RequestBody TaskEntity task) {
//        return taskService.editTask(id, task);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteTask(@PathVariable Long id) {
//        taskService.deleteTask(id);
//    }
//
//    // Exception handler for TaskNotFoundException
//    @ExceptionHandler(TaskNotFoundException.class)
//    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
}

