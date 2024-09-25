package dev.backendintegratedproject.services;

import dev.backendintegratedproject.dtos.BoardDTO;
import dev.backendintegratedproject.managements.entities.BoardEntity;
<<<<<<< Updated upstream
import dev.backendintegratedproject.managements.entities.StatusEntity;
import dev.backendintegratedproject.managements.entities.TaskV3Entity;
=======
import dev.backendintegratedproject.managements.entities.UserMainEntity;
>>>>>>> Stashed changes
import dev.backendintegratedproject.managements.repositories.BoardRepository;
import dev.backendintegratedproject.managements.repositories.StatusRepository;
import dev.backendintegratedproject.managements.repositories.TaskV3Repository;
import dev.backendintegratedproject.securities.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskV3Repository taskRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // ดึง userId จาก JWT token
    private String getUserIdFromToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    // สร้าง board ใหม่
    public BoardDTO createBoard(String boardName, String token) {
        String userId = getUserIdFromToken(token);
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardName(boardName);
        boardEntity.setOwnerId(userId);
        boardEntity.setCreatedOn(new Date());
        boardEntity.setUpdatedOn(new Date());
        BoardEntity savedBoard = boardRepository.save(boardEntity);

        return modelMapper.map(savedBoard, BoardDTO.class);
    }

    // ดึง board ตาม id
    public Optional<BoardDTO> getBoardById(String boardId, String token) {
        String userId = getUserIdFromToken(token);
        return boardRepository.findById(boardId)
                .filter(board -> board.getOwnerId().equals(userId))
                .map(board -> modelMapper.map(board, BoardDTO.class));
    }

    // ดึง board ทั้งหมดสำหรับผู้ใช้
    public List<BoardDTO> getBoardsForUser(String token) {
        String userId = getUserIdFromToken(token);
        return boardRepository.findAllByOwnerId(userId).stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());
    }

    // อัปเดต board
    public BoardDTO updateBoard(String boardId, BoardDTO boardDTO, String token) {
        String userId = getUserIdFromToken(token);
        BoardEntity existingBoard = boardRepository.findById(boardId)
                .filter(board -> board.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        existingBoard.setBoardName(boardDTO.getName());
        existingBoard.setUpdatedOn(new Date());

        BoardEntity updatedBoard = boardRepository.save(existingBoard);
        return modelMapper.map(updatedBoard, BoardDTO.class);
    }

    // ลบ board
    public void deleteBoard(String boardId, String token) {
        String userId = getUserIdFromToken(token);
        BoardEntity existingBoard = boardRepository.findById(boardId)
                .filter(board -> board.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        boardRepository.delete(existingBoard);
    }

    // ดึง tasks ของ board
    public List<TaskV3Entity> getTasksForBoard(String boardId, String token, String sortBy, List<String> filterStatuses) {
        String userId = getUserIdFromToken(token);

        BoardEntity board = boardRepository.findById(boardId)
                .filter(b -> b.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        List<TaskV3Entity> tasks = taskRepository.findByBoardId(board);

        if (filterStatuses != null && !filterStatuses.isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> filterStatuses.contains(task.getStatusId().getStatusName()))
                    .collect(Collectors.toList());
        }

        if ("createdOn".equalsIgnoreCase(sortBy)) {
            tasks = tasks.stream()
                    .sorted((t1, t2) -> t1.getCreatedOn().compareTo(t2.getCreatedOn()))
                    .collect(Collectors.toList());
        } else if ("updatedOn".equalsIgnoreCase(sortBy)) {
            tasks = tasks.stream()
                    .sorted((t1, t2) -> t1.getUpdatedOn().compareTo(t2.getUpdatedOn()))
                    .collect(Collectors.toList());
        }

        return tasks;
    }

    // ดึง task ตาม id
    public TaskV3Entity getTaskById(String boardId, Integer taskId, String token) {
        String userId = getUserIdFromToken(token);

        BoardEntity board = boardRepository.findById(boardId)
                .filter(b -> b.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        return (TaskV3Entity) taskRepository.findByTaskIdAndBoardId(taskId, board)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    // สร้าง task ใหม่
    @Transactional
    public TaskV3Entity createTask(String boardId, TaskV3Entity newTask, String token) {
        String userId = getUserIdFromToken(token);

        BoardEntity board = boardRepository.findById(boardId)
                .filter(b -> b.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        newTask.setBoardId(board);
        newTask.setCreatedOn(new Date());
        newTask.setUpdatedOn(new Date());

        if (newTask.getStatusId() != null) {
            StatusEntity status = statusRepository.findById(newTask.getStatusId().getStatusId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status not found"));
            newTask.setStatusId(status);
        }

        return taskRepository.save(newTask);
    }

    // อัปเดต task
    @Transactional
    public TaskV3Entity updateTask(String boardId, Integer taskId, TaskV3Entity updateTask, String token) {
        String userId = getUserIdFromToken(token);

        BoardEntity board = boardRepository.findById(boardId)
                .filter(b -> b.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        TaskV3Entity task = (TaskV3Entity) taskRepository.findByTaskIdAndBoardId(taskId, board)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTaskTitle(updateTask.getTaskTitle());
        task.setTaskDescription(updateTask.getTaskDescription());
        task.setTaskAssignees(updateTask.getTaskAssignees());
        task.setUpdatedOn(new Date());


        if (updateTask.getStatusId() != null) {
            StatusEntity status = statusRepository.findById(updateTask.getStatusId().getStatusId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status not found"));
            task.setStatusId(status);
        }

        return taskRepository.save(task);
    }

    // ลบ task
    public void deleteTask(String boardId, Integer taskId, String token) {
        String userId = getUserIdFromToken(token);

        BoardEntity board = boardRepository.findById(boardId)
                .filter(b -> b.getOwnerId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Board not found or unauthorized"));

        TaskV3Entity task = (TaskV3Entity) taskRepository.findByTaskIdAndBoardId(taskId, board)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    public BoardEntity saveBoard(BoardEntity board) {
        return boardRepository.save(board);
    }


    public Optional<UserMainEntity> getUserById(String oid) {
        return boardRepository.findById(oid).map(BoardEntity::getOwner);
    }
}
