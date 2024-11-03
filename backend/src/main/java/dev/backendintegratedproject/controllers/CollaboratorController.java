package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.dtos.board.CollaboratorRequestDTO;
import dev.backendintegratedproject.services.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;

    @GetMapping("/{boardId}/collabs")
    public ResponseEntity<List<BoardPermissionDTO>> getCollaborators(@PathVariable String boardId, Authentication authentication) {
        // Validate user access to board

        List<BoardPermissionDTO> collaborators = collaboratorService.getCollaborators(boardId);
        return ResponseEntity.ok(collaborators);
    }

    @GetMapping("/{boardId}/collabs/{userId}")
    public ResponseEntity<BoardPermissionDTO> getCollaboratorInfo(@PathVariable String boardId,
                                                                  @PathVariable String userId,
                                                                  Authentication authentication) {
        // Validate user access to board

        BoardPermissionDTO collaborator = collaboratorService.getCollaboratorInfo(boardId, userId);
        return ResponseEntity.ok(collaborator);
    }

    @PostMapping("/{boardId}/collabs")
    public ResponseEntity<?> addCollaborator(@PathVariable String boardId, @RequestBody CollaboratorRequestDTO collaboratorRequest, Authentication authentication) {
        // Validate user ownership of board

        collaboratorService.addCollaborator(boardId, collaboratorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Collaborator added successfully");
    }
}
