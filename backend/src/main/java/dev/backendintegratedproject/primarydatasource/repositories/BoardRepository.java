package dev.backendintegratedproject.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import dev.backendintegratedproject.primarydatasource.entities.Board;

import java.util.List;

public interface                BoardRepository extends JpaRepository<Board, String> {

    List<Board> findAllByOwnerID(String userID);


}
