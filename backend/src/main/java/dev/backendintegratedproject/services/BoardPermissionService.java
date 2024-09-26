package dev.backendintegratedproject.services;

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
}
