package dev.backendintegratedproject.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import dev.backendintegratedproject.primarydatasource.entities.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String> {

    // Find all boards by the owner's ID
    List<Board> findAllByOwnerID(String userID);

    // Get the visibility status (isPublic) of a board by its ID
    @Query("SELECT b.visibility FROM Board b WHERE b.id = :boardID")
    Boolean getIsPublicByBoardID(@Param("boardID") String boardID);

    // Update the visibility of a board
    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.visibility = :visibility WHERE b.id = :boardID")
    void setVisibility(@Param("boardID") String boardID, @Param("visibility") Boolean visibility);
}
