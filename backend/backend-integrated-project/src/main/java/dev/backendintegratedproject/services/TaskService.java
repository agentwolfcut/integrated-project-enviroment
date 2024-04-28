package dev.backendintegratedproject.services;

import dev.backendintegratedproject.entities.TaskEntity;
import dev.backendintegratedproject.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskEntity getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    //public TaskEntity getTaskById(Long id) {
    //        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    //    }

    //for add update delete
//    public TaskEntity createTask(TaskEntity task) {
//        task.setCreatedOn(new Date());
//        task.setUpdatedOn(new Date());
//        return taskRepository.save(task);
//    }
//
//    public TaskEntity updateTask(Long id, TaskEntity updatedTask) {
//        return taskRepository.findById(id)
//                .map(task -> {
//                    task.setTitle(updatedTask.getTitle());
//                    task.setDescription(updatedTask.getDescription());
//                    task.setAssignedTo(updatedTask.getAssignedTo());
//                    task.setStatus(updatedTask.getStatus());
//                    task.setUpdatedOn(new Date());
//                    return taskRepository.save(task);
//                })
//                .orElseThrow(() -> new TaskNotFoundException(id));
//    }
//
//    public void deleteTask(Long id) {
//        if (!taskRepository.existsById(id)) {
//            throw new TaskNotFoundException(id);
//        }
//        taskRepository.deleteById(id);
//    }
}
