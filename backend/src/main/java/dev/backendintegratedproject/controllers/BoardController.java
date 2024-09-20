package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.BoardDTO;
import dev.backendintegratedproject.dtos.TaskDTO;
import dev.backendintegratedproject.managements.entities.TaskV3Entity;
import dev.backendintegratedproject.services.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/v2/boards")
@CrossOrigin(origins = {"http://localhost:5173", "http://ip23kk3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
public class BoardController {

    @Autowired
    private BoardService boardService;

    // Create a new board
    @PostMapping("")
    public ResponseEntity<?> createBoard(@RequestBody Map<String, String> boardRequest, @RequestHeader("Authorization") String token) {
        String boardName = boardRequest.get("boardName");

        try {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            BoardDTO boardDTO = boardService.createBoard(boardName, jwtToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(boardDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create board: " + e.getMessage()));
        }
    }

    // Get all boards for the authenticated user
    @GetMapping("")
    public ResponseEntity<?> getBoards(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix

        try {
            List<BoardDTO> boardDTO = boardService.getBoardsForUser(jwtToken);
            if (boardDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(boardDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access: " + e.getMessage()));
        }
    }

    // Get a specific board by its ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@PathVariable String id, @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix

        try {
            Optional<BoardDTO> boardDTO = boardService.getBoardById(id, jwtToken);
            if (boardDTO.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(boardDTO.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Board not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve board: " + e.getMessage()));
        }
    }

    // Get tasks for a specific board, with optional sorting and filtering
    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getTasksForBoard(@PathVariable String id,
                                              @RequestHeader("Authorization") String token,
                                              @RequestParam(required = false) String sortBy,
                                              @RequestParam(required = false) List<String> filterStatuses) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        try {
            List<TaskV3Entity> taskV3Entities = boardService.getTasksForBoard(id, jwtToken, sortBy, filterStatuses);
            return ResponseEntity.status(HttpStatus.OK).body(taskV3Entities);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve tasks: " + e.getMessage()));
        }
    }

    // Get a specific task by task ID within a board
    @GetMapping("/{boardId}/tasks/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable String boardId,
                                         @PathVariable String taskId,
                                         @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        try {
            TaskV3Entity taskDTO = boardService.getTaskById(boardId, Integer.parseInt(taskId), jwtToken);
            return ResponseEntity.status(HttpStatus.OK).body(taskDTO);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve task: " + e.getMessage()));
        }
    }

    // Delete a specific task from a board
    @DeleteMapping("/{boardId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String boardId,
                                        @PathVariable Integer taskId,
                                        @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        try {
            boardService.deleteTask(boardId, taskId, jwtToken);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete task: " + e.getMessage()));
        }
    }
}
