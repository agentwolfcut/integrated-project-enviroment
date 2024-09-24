package dev.backendintegratedproject.services;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.entities.TaskEntity;
import dev.backendintegratedproject.managements.repositories.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    //Get all boards


    //Get all boards
    public Iterable<BoardEntity> getAllBoards() {
        return boardRepository.findAll();
    }

    //Delete a board
    public void deleteBoard(String id) { // changed to String
        boardRepository.deleteById(id);
    }

    public Optional<BoardEntity> getBoardById(String id) { // changed to String
        return boardRepository.findById(id);
    }
}
