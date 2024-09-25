package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.BoardDTO;
import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.services.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<BoardEntity> boardEntities = (List<BoardEntity>) boardService.getAllBoards();
        List<BoardDTO> boardDTOs = boardEntities.stream().map(board -> {

            BoardDTO.UserMainEntity ownerDTO = new BoardDTO.UserMainEntity(
                    board.getOwner().getOid(),
                    board.getOwner().getName()
            );

            return new BoardDTO(
                    board.getId(),
                    board.getName(),
                    ownerDTO
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(boardDTOs);
    }


    // Get board by ID


    // Get board by ID, but restrict access to the owner only
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable String id) {
        // Get the current authenticated user's oid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserOid = (String) authentication.getPrincipal();  // Assuming oid is stored in the principal

        // Find the board by id
        Optional<BoardEntity> boardEntity = boardService.getBoardById(id);
        if (boardEntity.isPresent()) {
            BoardEntity board = boardEntity.get();

            // Check if the current user is the owner of the board
            if (!board.getOwner().getOid().equals(currentUserOid)) {
                return ResponseEntity.status(403).build();  // Return 403 Forbidden if not the owner
            }

            // If the user is the owner, return the board details
            BoardDTO boardDTO = new BoardDTO(
                    board.getId(),
                    board.getName(),
                    new BoardDTO.UserMainEntity(board.getOwner().getOid(), board.getOwner().getName())
            );
            return ResponseEntity.ok(boardDTO);
        }

        // If the board is not found, return 404
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

    // Create a board
}
