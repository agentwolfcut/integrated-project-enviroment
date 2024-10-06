package dev.backendintegratedproject.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import dev.backendintegratedproject.primarydatasource.entities.Board;

import java.util.List;
import java.util.Map;

public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findAllByOwnerID(String userID);

    @Query("SELECT b.visiblity FROM Board b WHERE b.id = :id")
    Boolean getIsPublicByBoardID(@Param("id") String boardID);

    @Modifying
    @Query(value = "UPDATE Board b SET b.visiblity = :vis WHERE b.id = :bID")
    void setVisibility(@Param("bID") String boardID, @Param("vis") Boolean newVis);

}
