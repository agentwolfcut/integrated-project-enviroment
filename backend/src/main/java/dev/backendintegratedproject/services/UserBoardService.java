package dev.backendintegratedproject.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import dev.backendintegratedproject.dtos.board.VisibilityDTO;
import dev.backendintegratedproject.primarydatasource.entities.Board;
import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import dev.backendintegratedproject.primarydatasource.repositories.PrimaryUserRepository;
import dev.backendintegratedproject.primarydatasource.repositories.StatusRepository;
import dev.backendintegratedproject.primarydatasource.repositories.TaskRepository;

import java.util.List;

@Service
public class UserBoardService {

    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<Board> getBoardsByUserID(String userID) {
        return boardRepository.findAllByOwnerID(userID);
    }

    public Board getBoardsDetail(String boardID) {
        return boardRepository.findById(boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
    }

    @Transactional
    public Board createBoardForUser(String userID, String boardName) {
        PrimaryUser user = primaryUserRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + userID + " does not exist !!!"));

        Board newBoard = new Board();
        newBoard.setName(boardName);
        newBoard.setOwnerID(user.getOid());
        newBoard.setVisibility("PRIVATE");  // Default to PRIVATE

        Board createdBoard = boardRepository.save(newBoard);
        createdBoard.setOwner(user);
        return createdBoard;
    }

    @Transactional
    public Board updateBoard(String boardID, Board board) {
        Board boardToUpdate = boardRepository.findById(boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board " + boardID + " does not exist !!!"));

        boardToUpdate.setName(board.getName());
        boardRepository.save(boardToUpdate);
        return boardToUpdate;
    }

    @Transactional
    public Board deleteBoard(String boardID) {
        Board board = boardRepository.findById(boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board " + boardID + " does not exist !!!"));

        taskRepository.deleteAll(taskRepository.findAllByBoardID(boardID));
        statusRepository.deleteAll(statusRepository.findAllByBoardID(boardID));
        boardRepository.delete(board);
        return board;
    }

    public String generateBoardID() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 10);
    }

    public boolean existsById(String boardID) {
        return boardRepository.existsById(boardID);
    }

    @Transactional
    public void setVisibility(String boardID, VisibilityDTO visibility, String userID) {
        // Check board existence and if the user is the owner
        Board board = boardRepository.findById(boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        if (!board.getOwnerID().equals(userID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to change this board's visibility.");
        }

        // Check and set the new visibility
        Boolean newVis = null;
        switch (visibility.getVisibility().toLowerCase()) {
            case "public": newVis = true;
                break;
            case "private": newVis = false;
                break;
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid visibility value");
        }
        boardRepository.setVisibility(boardID, newVis);
    }
    public void checkBoardAccess(Board board, String userID) {
        if (!board.isPublic() && !board.getOwnerID().equals(userID)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this board.");
        }
    }




}
