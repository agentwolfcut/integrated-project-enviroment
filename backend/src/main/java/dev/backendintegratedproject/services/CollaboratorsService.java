package dev.backendintegratedproject.services;

import dev.backendintegratedproject.primarydatasource.entities.AccessRight;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.Collaborators;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.primarydatasource.repositories.CollaboratorsRepository;

import java.util.List;

@Service
public class CollaboratorsService {

    @Autowired
    private CollaboratorsRepository collaboratorsRepository;

    @Autowired
    private BoardRepository boardRepository;



    public Collaborators addCollaborator(String userOid, String boardID, String accessRight) {
        if (collaboratorsRepository.existsByUserOidAndBoardID(userOid, boardID)) {
            throw new IllegalArgumentException("User is already a collaborator for this board.");
        }

        // ตรวจสอบค่าของ AccessRight
        if (!accessRight.equalsIgnoreCase("READ") && !accessRight.equalsIgnoreCase("WRITE")) {
            throw new IllegalArgumentException("Invalid access right. Use READ or WRITE.");
        }

        // เพิ่มสิทธิ์ Collaborator
        Collaborators newAccessRight = new Collaborators();
        newAccessRight.setUserOid(userOid);
        newAccessRight.setBoardID(boardID);
        newAccessRight.setAccessRight(AccessRight.valueOf(accessRight.toUpperCase()));

        return collaboratorsRepository.save(newAccessRight);
    }


    public void removeCollaborator(String userOid, String boardID) {
        if (!collaboratorsRepository.existsByUserOidAndBoardID(userOid, boardID)) {
            throw new IllegalArgumentException("User is not a collaborator for this board.");
        }

        collaboratorsRepository.deleteByUserOidAndBoardID(userOid, boardID);
    }
    //updateCollaborator
    public Collaborators updateCollaborator(String userOid, String boardID, String accessRight) {
        if (!collaboratorsRepository.existsByUserOidAndBoardID(userOid, boardID)) {
            throw new IllegalArgumentException("User is not a collaborator for this board.");
        }

        // ตรวจสอบค่าของ AccessRight
        if (!accessRight.equalsIgnoreCase("READ") && !accessRight.equalsIgnoreCase("WRITE")) {
            throw new IllegalArgumentException("Invalid access right. Use READ or WRITE.");
        }

        Collaborators newAccessRight = new Collaborators();
        newAccessRight.setUserOid(userOid);
        newAccessRight.setBoardID(boardID);
        newAccessRight.setAccessRight(AccessRight.valueOf(accessRight.toUpperCase()));

        return collaboratorsRepository.save(newAccessRight);
    }

    public List<Collaborators> getCollaborators(String boardID) {
        return collaboratorsRepository.findAllByBoardID(boardID);
    }

    public boolean isBoardOwner(String oid, String boardID) {
        Board board = boardRepository.findById(boardID).orElseThrow(() -> new IllegalArgumentException("Board not found."));
        return board.getOwnerID().equals(oid);
    }
}


