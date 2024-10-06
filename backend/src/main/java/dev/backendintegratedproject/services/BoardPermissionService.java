package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.primarydatasource.repositories.BoardPermissionRepository;

@Service
public class BoardPermissionService {
    @Autowired
    BoardPermissionRepository boardPermissionRepository;

    public BoardPermissionDTO getPermission(String oid, String boardID){
        BoardPermissionDTO bpDTO = new BoardPermissionDTO();
        bpDTO.setPermission(boardPermissionRepository.getPermission(oid, boardID));
        return bpDTO;
    }
    public boolean canAccessBoard(UserDetailsDTO userDetails, Board board) {
        // Allow access if the board is public or if the user is the owner
        return board.isPublic() || userDetails.getOid().equals(board.getOwnerID());
    }



}
