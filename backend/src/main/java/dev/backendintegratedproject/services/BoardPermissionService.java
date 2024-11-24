package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.dtos.board.BoardPermissionDTO;
import dev.backendintegratedproject.primarydatasource.repositories.BoardPermissionRepository;

import java.util.List;

@Service
public class BoardPermissionService {

    @Autowired
    private BoardPermissionRepository boardPermissionRepository;

    @Autowired
    private BoardRepository boardRepository;



    public BoardPermission addCollaborator(String userOid, String boardID, String permission) {
        if (boardPermissionRepository.existsByUserIDAndBoardID(userOid, boardID)) {
            throw new IllegalArgumentException("User is already a collaborator for this board.");
        }

        // ตรวจสอบค่าของ AccessRight
        if (!permission.equalsIgnoreCase("READ") && !permission.equalsIgnoreCase("WRITE")) {
            throw new IllegalArgumentException("Invalid access right. Use READ or WRITE.");
        }

        // เพิ่มสิทธิ์ Collaborator
        BoardPermission newPermission = new BoardPermission();
        newPermission.setUserID(userOid);
        newPermission.setBoardID(boardID);
        newPermission.setPermission(permission.toUpperCase());

        return boardPermissionRepository.save(newPermission);
    }

    public void removeCollaborator(String userID, String boardID) {
        if (!boardPermissionRepository.existsByUserIDAndBoardID(userID, boardID)) {
            throw new IllegalArgumentException("User is not a collaborator for this board.");
        }

        boardPermissionRepository.deleteByUserIDAndBoardID(userID, boardID);
    }

    public BoardPermission updateCollaborator(String userID, String boardID, String permission) {
        if (!boardPermissionRepository.existsByUserIDAndBoardID(userID, boardID)) {
            throw new IllegalArgumentException("User is not a collaborator for this board.");
        }

        // ตรวจสอบค่าของ AccessRight
        if (!permission.equalsIgnoreCase("READ") && !permission.equalsIgnoreCase("WRITE")) {
            throw new IllegalArgumentException("Invalid access right. Use READ or WRITE.");
        }

        // อัพเดทสิทธิ์ Collaborator
        BoardPermission updatedPermission = boardPermissionRepository.findByUserIDAndBoardID(userID, boardID);
        updatedPermission.setPermission(permission.toUpperCase());

        return boardPermissionRepository.save(updatedPermission);
    }

    public List<BoardPermission> getCollaborators(String boardID) {
        return boardPermissionRepository.findAllByBoardID(boardID);
    }

    public boolean isBoardOwner(String oid, String boardID) {
        Board board = boardRepository.findById(boardID).orElseThrow(() -> new IllegalArgumentException("Board not found."));
        return board.getOwnerID().equals(oid);
    }
}


