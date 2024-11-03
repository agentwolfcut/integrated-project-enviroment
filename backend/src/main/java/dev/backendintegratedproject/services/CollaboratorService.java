package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.dtos.board.CollaboratorRequestDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermissionId;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import dev.backendintegratedproject.primarydatasource.repositories.BoardPermissionRepository;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import dev.backendintegratedproject.primarydatasource.repositories.PrimaryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaboratorService {

    @Autowired
    private BoardPermissionRepository boardPermissionRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    public List<BoardPermissionDTO> getCollaborators(String boardId) {
        return boardPermissionRepository.findAllByBoard_Id(boardId)
                .stream()
                .map(permission -> new BoardPermissionDTO(
                        permission.getPrimaryUser().getOid(),
                        permission.getPrimaryUser().getUsername(),
                        permission.getPrimaryUser().getEmail(),
                        permission.getPermission(),
                        permission.getAddedOn()
                ))
                .collect(Collectors.toList());
    }

    public BoardPermissionDTO getCollaboratorInfo(String boardId, String collabOid) {
        BoardPermission permission = boardPermissionRepository
                .findById(new BoardPermissionId(collabOid, boardId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator not found"));

        return new BoardPermissionDTO(
                permission.getPrimaryUser().getOid(),
                permission.getPrimaryUser().getUsername(),
                permission.getPrimaryUser().getEmail(),
                permission.getPermission(),
                permission.getAddedOn()
        );
    }

    public void addCollaborator(String boardId, CollaboratorRequestDTO collaboratorRequest) {
        PrimaryUser user = (PrimaryUser) primaryUserRepository.findByEmail(collaboratorRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        if (!collaboratorRequest.getAccessRight().equals("READ") && !collaboratorRequest.getAccessRight().equals("WRITE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid access right");
        }

        BoardPermissionId id = new BoardPermissionId(user.getOid(), board.getId());
        if (boardPermissionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Collaborator already exists");
        }

        BoardPermission permissions = new BoardPermission(id, collaboratorRequest.getAccessRight(), user, board, LocalDateTime.now());
        boardPermissionRepository.save(permissions);
    }
}
