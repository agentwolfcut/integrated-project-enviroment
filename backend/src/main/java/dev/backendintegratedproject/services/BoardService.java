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
    private final BoardRepository boardRepository;

    // TEST
    public Optional<BoardEntity> getBoardById(String boardId) {
        return boardRepository.findById(boardId);
    }


    public Object getAllBoards() {
        return boardRepository.findAll();
    }

    public Object addBoard(BoardEntity board) {
        return boardRepository.save(board);
    }

    public Object editBoard(String boardId, BoardEntity board) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            BoardEntity boardEntity = boardOptional.get();
            boardEntity.setBoardName(board.getBoardName());
            return boardRepository.save(boardEntity);
        } else {
            return "Board not found";
        }
    }

    public Object deleteBoard(String boardId) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            boardRepository.deleteById(boardId);
            return "Board deleted";
        } else {
            return "Board not found";
        }
    }

    public Object getAllTasksInBoard(String boardId) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            return boardOptional.get().getTasks();
        } else {
            return "Board not found";
        }
    }

    public Object addTaskToBoard(String boardId, TaskEntity task) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            BoardEntity boardEntity = boardOptional.get();
            boardEntity.getTasks().add(task);
            return boardRepository.save(boardEntity);
        } else {
            return "Board not found";
        }
    }

    public Object getTaskById(String boardId, Integer taskId) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            BoardEntity boardEntity = boardOptional.get();
            for (TaskEntity task : boardEntity.getTasks()) {
                if (task.getTaskId().equals(taskId)) {
                    return task;
                }
            }
            return "Task not found";
        } else {
            return "Board not found";
        }
    }

    public Object editTask(String boardId, Integer taskId, TaskEntity task) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            BoardEntity boardEntity = boardOptional.get();
            for (TaskEntity taskEntity : boardEntity.getTasks()) {
                if (taskEntity.getTaskId().equals(taskId)) {
                    taskEntity.setTitle(task.getTitle());
                    taskEntity.setDescription(task.getDescription());
                    taskEntity.setAssignees(task.getAssignees());
                    taskEntity.setStatus(task.getStatus());
                    return boardRepository.save(boardEntity);
                }
            }
            return "Task not found";
        } else {
            return "Board not found";
        }
    }

    public Object deleteTask(String boardId, Integer taskId) {
        Optional<BoardEntity> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            BoardEntity boardEntity = boardOptional.get();
            for (TaskEntity task : boardEntity.getTasks()) {
                if (task.getTaskId().equals(taskId)) {
                    boardEntity.getTasks().remove(task);
                    return boardRepository.save(boardEntity);
                }
            }
            return "Task not found";
        } else {
            return "Board not found";
        }
    }
}
