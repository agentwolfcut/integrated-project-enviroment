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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import dev.backendintegratedproject.primarydatasource.repositories.CollaboratorsRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
        return collaboratorsRepository.findByBoardIDAndUserOid(id, userOid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this board."));
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
        // ตรวจสอบค่า Access Right ว่า valid หรือไม่
        if (!input.getAccessRight().equalsIgnoreCase("READ") && !input.getAccessRight().equalsIgnoreCase("WRITE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid access right. Use READ or WRITE.");
        }

        // ตรวจสอบว่า User มีสิทธิ์หรือไม่
        User user = userService.findByEmail(input.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with specified email not found.");
        }

        if (collaboratorsRepository.existsByUserOidAndBoardID(user.getOid(), board.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user is already a collaborator or pending collaborator.");
        }

        if (board.getOwnerID().equals(user.getOid())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The board owner cannot be added as a collaborator.");
        }

        // เพิ่ม collaborator
        Collaborators newCollab = new Collaborators();
        newCollab.setUserOid(user.getOid());
        newCollab.setBoardID(board.getId());
        newCollab.setAccessRight(AccessRight.valueOf(input.getAccessRight().toUpperCase()));
        return collaboratorsRepository.save(newCollab);
    }
    public boolean hasWriteAccess(String userOid, String boardID) {
        return collaboratorsRepository.findByBoardIDAndUserOid(boardID, userOid)
                .map(collaborator -> collaborator.getAccessRight() == AccessRight.WRITE)
                .orElse(false);
    }

    // เพิ่มเมธอดตรวจสอบ AccessRight
    private boolean isValidAccessRight(String accessRight) {
        return "READ".equalsIgnoreCase(accessRight) || "WRITE".equalsIgnoreCase(accessRight);
    }
    public boolean hasReadAccess(String userOid, String boardID) {
        return collaboratorsRepository.findByBoardIDAndUserOid(boardID, userOid)
                .map(collaborator -> collaborator.getAccessRight() == AccessRight.READ || collaborator.getAccessRight() == AccessRight.WRITE)
                .orElse(false);
    }

    public Collaborators deleteCollab(String id, String userOid) {
        Collaborators collab = getCollabOfBoard(id, userOid, true);
        collaboratorsRepository.delete(collab);
        return collab;
    }

    public List<Collaborators> getAllCollabByOid(String userOid) {
        return collaboratorsRepository.findAllByUserOid(userOid);
    }

    public Optional<Collaborators> getCollabByBoardIdAndUserOid(String boardId, String userOid) {
        return collaboratorsRepository.findByBoardIDAndUserOid(boardId, userOid);
    }

    public Optional<Collaborators> getOptionalCollabOfBoard(String id, String userOid) {
        return collaboratorsRepository.findByBoardIDAndUserOid(id, userOid);
    }


}


