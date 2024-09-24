package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.securities.JwtTokenUtil;
import dev.backendintegratedproject.services.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://localhost:5173", "http://ip23kk3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //GET ALL BOARDS
    @GetMapping
    public ResponseEntity<Object> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    //Create Board
    @PostMapping
    public ResponseEntity<Object> addBoard(@RequestBody BoardEntity board) {
        return ResponseEntity.ok(boardService.addBoard(board));
    }

    //Get Board by ID
    @GetMapping("/{boardId}")
    public ResponseEntity<Object> getBoardById(@PathVariable String boardId) {
        Optional<BoardEntity> board = boardService.getBoardById(boardId);
        if (board.isPresent()) {
            return ResponseEntity.ok(board.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Edit Board
    @PutMapping("/{boardId}")
    public ResponseEntity<Object> editBoard(@PathVariable String boardId, @RequestBody BoardEntity board) {
        return ResponseEntity.ok(boardService.editBoard(boardId, board));
    }

    //Delete Board
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable String boardId) {
        return ResponseEntity.ok(boardService.deleteBoard(boardId));
    }

    //Get All Tasks in Board
    @GetMapping("/{boardId}/tasks")
    public ResponseEntity<Object> getAllTasksInBoard(@PathVariable String boardId) {
        return ResponseEntity.ok(boardService.getAllTasksInBoard(boardId));
    }

    //Add Task to Board
    @PostMapping("/{boardId}/tasks")
    public ResponseEntity<Object> addTaskToBoard(@PathVariable String boardId, @RequestBody TaskEntity task) {
        return ResponseEntity.ok(boardService.addTaskToBoard(boardId, task));
    }

    //Get Task by ID
    @GetMapping("/{boardId}/tasks/{taskId}")
    public ResponseEntity<Object> getTaskById(@PathVariable String boardId, @PathVariable Integer taskId) {
        return ResponseEntity.ok(boardService.getTaskById(boardId, taskId));
    }

    //Edit Task
    @PutMapping("/{boardId}/tasks/{taskId}")
    public ResponseEntity<Object> editTask(@PathVariable String boardId, @PathVariable Integer taskId, @RequestBody TaskEntity task) {
        return ResponseEntity.ok(boardService.editTask(boardId, taskId, task));
    }

    //Delete Task
    @DeleteMapping("/{boardId}/tasks/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable String boardId, @PathVariable Integer taskId) {
        return ResponseEntity.ok(boardService.deleteTask(boardId, taskId));
    }







}
