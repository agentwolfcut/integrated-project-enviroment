package dev.backendintegratedproject.services;

import dev.backendintegratedproject.managements.entities.BoardEntity;
import dev.backendintegratedproject.managements.repositories.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {

    @Autowired
    private final BoardRepository boardRepository;

    // TEST
    public Optional<BoardEntity> getBoardById(String boardId) {
        return boardRepository.findById(boardId);
    }


    public Object getAllBoards() {
        return boardRepository.findAll();
    }
}
