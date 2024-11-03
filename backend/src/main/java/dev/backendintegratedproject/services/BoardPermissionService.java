package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermissionId;
import dev.backendintegratedproject.primarydatasource.repositories.BoardPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoardPermissionService {

    @Autowired
    private BoardPermissionRepository boardPermissionRepository;

    public BoardPermissionDTO getPermission(String oid, String boardID) {
        String permission = boardPermissionRepository.getPermission(oid, boardID);

        if (permission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found for user on this board");
        }

        BoardPermissionDTO bpDTO = new BoardPermissionDTO();
        bpDTO.setPermission(permission);
        return bpDTO;
    }

    public boolean canAccessBoard(UserDetailsDTO userDetails, Board board) {
        // Allow access if the board is public or if the user is the owner
        if (board.isPublic() || userDetails.getOid().equals(board.getOwnerID())) {
            return true;
        }

        // Additional check for collaborator access by verifying permissions in BoardPermission
        BoardPermissionId permissionId = new BoardPermissionId(userDetails.getOid(), board.getId()); // ใช้ getId() แทน
        return boardPermissionRepository.existsById(permissionId);
    }

}
