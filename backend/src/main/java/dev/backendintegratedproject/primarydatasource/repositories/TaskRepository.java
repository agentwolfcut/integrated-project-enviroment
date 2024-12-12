package dev.backendintegratedproject.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import dev.backendintegratedproject.primarydatasource.entities.Status;
import dev.backendintegratedproject.primarydatasource.entities.Task;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByStatus(Status status);

    List<Task> findByStatus_NameIn(Collection<String> statusNames);

    List<Task> findAllByBoardIDAndStatusNameIn(String boardID, Collection<String> statusNames);

    List<Task> findAllByBoardID (String boardID);

    @Query(value = "select count(*) from tasks where taskStatus = :id ", nativeQuery = true)
    Integer countByStatus(Integer id);

    @Modifying
    @Query(value = "update tasks set taskStatus = :newStat where taskStatus = :delStat", nativeQuery = true)
    void transferStatusTasks(Integer delStat, Integer newStat);

    Task findByIdAndBoardID(Integer id, String boardID);

    List<Task> findAllByBoardIDAndStatusNameIn(String boardID, List<String> statusNames);


}
