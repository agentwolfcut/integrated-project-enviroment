package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.BoardDTO;
import dev.backendintegratedproject.managements.entities.BoardEntity;
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

    // Get all boards
    @GetMapping
    public ResponseEntity<Object> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    // Get board by ID
    // Get board by ID
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable String id) {
        Optional<BoardEntity> boardEntity = boardService.getBoardById(id);
        if (boardEntity.isPresent()) {
            // Mapping from BoardEntity to BoardDTO
            BoardEntity board = boardEntity.get();
            BoardDTO boardDTO = new BoardDTO(
                    board.getId(),
                    board.getName(),
                    new BoardDTO.UserMainEntity(board.getOwner().getOid(), board.getOwner().getName())
            );
            return ResponseEntity.ok(boardDTO);
        }
        return ResponseEntity.notFound().build();
    }


    // Delete a board
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBoard(@PathVariable String id) {
        Optional<BoardEntity> board = boardService.getBoardById(id);
        if (board.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

    // Utility method to map BoardEntity to BoardDTO



}
