package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.services.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/v2/boards")
@CrossOrigin(origins = {"http://localhost:5173", "http://ip23kk3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
public class BoardController {
    @Autowired
    private BoardService boardService;


    //Getall
    @GetMapping
    public ResponseEntity<Object> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
    // TEST
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardEntity> getBoardById(@PathVariable String boardId) {
        Optional<BoardEntity> board = boardService.getBoardById(boardId);
        if (board.isPresent()) {
            return ResponseEntity.ok(board.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
