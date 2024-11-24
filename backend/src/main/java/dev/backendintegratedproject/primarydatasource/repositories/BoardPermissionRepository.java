package dev.backendintegratedproject.primarydatasource.repositories;

import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;

import java.util.List;

public interface BoardPermissionRepository extends JpaRepository<BoardPermission, String> {

    @Query("SELECT COUNT(bp) > 0 FROM BoardPermission bp WHERE bp.userID = :uid AND bp.boardID = :bid")
    Boolean checkBoardAccess(@Param("uid") String userId, @Param("bid") String boardId);

    @Query("SELECT bp.permission FROM BoardPermission bp WHERE bp.userID = :uid AND bp.boardID = :bid")
    String getPermission(@Param("uid") String userId, @Param("bid") String boardId);

    // เพิ่ม Method สำหรับตรวจสอบการมีสิทธิ์ของ User
    boolean existsByUserIDAndBoardID(String userId, String boardId);

    void deleteByUserIDAndBoardID(String userID, String boardID);

    BoardPermission findByUserIDAndBoardID(String userID, String boardID);

    List<BoardPermission> findAllByBoardID(String boardID);
}

