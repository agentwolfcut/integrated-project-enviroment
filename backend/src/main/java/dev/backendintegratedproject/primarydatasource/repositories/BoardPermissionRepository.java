package dev.backendintegratedproject.primarydatasource.repositories;

import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardPermissionRepository extends JpaRepository<BoardPermission, BoardPermissionId> {

    @Query("SELECT COUNT(bp) > 0 FROM BoardPermission bp WHERE bp.id.userId = :uid AND bp.id.boardId = :bid")
    Boolean checkBoardAccess(@Param("uid") String userId, @Param("bid") String boardId);

    @Query("SELECT bp.permission FROM BoardPermission bp WHERE bp.id.userId = :uid AND bp.id.boardId = :bid")
    String getPermission(@Param("uid") String userId, @Param("bid") String boardId);

    // แก้ชื่อ method เป็น findAllByBoard_Id เพื่อให้สอดคล้องกับชื่อ field ใน entity Board
    List<BoardPermission> findAllByBoard_Id(String boardId);
}

