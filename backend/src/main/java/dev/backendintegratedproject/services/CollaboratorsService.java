package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.board.AccessRightDTO;
import dev.backendintegratedproject.dtos.board.CollabCreateInputDTO;
import dev.backendintegratedproject.dtos.board.CollabOutputDTO;
import dev.backendintegratedproject.primarydatasource.entities.AccessRight;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.Collaborators;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import dev.backendintegratedproject.userdatasource.entities.User;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;


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

    public Collaborators getCollabOfBoard(String id, String userOid, boolean b) {
        return (Collaborators) collaboratorsRepository.findByBoardIDAndUserOid(id, userOid).orElseThrow(() -> new IllegalArgumentException("User is not a collaborator for this board."));
    }
    public List<Collaborators> getAllCollabOfBoard(String boardID) {
        return collaboratorsRepository.findAllByBoardID(boardID);
    }

    public CollabOutputDTO mapOutputDTO(Collaborators collab) {//input board must have oid!!
        CollabOutputDTO collabOutputDTO = modelMapper.map(collab, CollabOutputDTO.class);
        User user = userService.findByOid(collab.getUserOid());
        collabOutputDTO.setName(user.getName());
        collabOutputDTO.setEmail(user.getEmail());
        collabOutputDTO.setOid(user.getOid());
        return collabOutputDTO;
    }

    public Collaborators updateCollab(String id, String collabOid, AccessRightDTO input) {
        Collaborators collab = getCollabOfBoard(id, collabOid, true);
        collab.setAccessRight(input.getAccessRight());
        return collaboratorsRepository.save(collab);
    }

    public Collaborators createNewCollab(Board board, CollabCreateInputDTO input) {
        if (input == null || !input.getAccessRight().equalsIgnoreCase("WRITE") && !input.getAccessRight().equalsIgnoreCase("READ")) {
            throw new IllegalArgumentException("Request body is missing or unreadable");
        }

        User user = userService.findByEmail(input.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User with specified email not found.");
        }
        if (collaboratorsRepository.existsByUserOidAndBoardID(user.getOid(), board.getId())) {
            throw new IllegalArgumentException("This user is already the collaborator or pending collaborator on this board.");
        }
        if (board.getOwnerID().equals(user.getOid())) {
            throw new IllegalArgumentException("The board owner cannot be added as a collaborator.");
        }

        Collaborators newCollab = new Collaborators();
        newCollab.setUserOid(user.getOid());
        newCollab.setBoardID(board.getId());
        newCollab.setAccessRight(AccessRight.valueOf(input.getAccessRight()));
        return collaboratorsRepository.save(newCollab);
    }

    public Collaborators deleteCollab(String id, String userOid) {
        Collaborators collab = getCollabOfBoard(id, userOid, true);
        collaboratorsRepository.delete(collab);
        return collab;
    }
}


