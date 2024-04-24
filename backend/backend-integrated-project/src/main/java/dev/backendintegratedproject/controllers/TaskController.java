package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/itb-kk/v1/tasks")
@CrossOrigin(origins = "http://ip23kk3.sit.kmutt.ac.th:8080"

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
}
