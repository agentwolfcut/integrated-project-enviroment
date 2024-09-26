package dev.backendintegratedproject.primarydatasource.repositories;

import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dev.backendintegratedproject.primarydatasource.entities.BoardPermission;

public interface BoardPermissionRepository extends JpaRepository<BoardPermission, String> {
    @Query("SELECT EXISTS(SELECT bp FROM BoardPermission bp WHERE bp.userID = :uid AND bp.boardID = :bid)")
    Boolean checkBoardAccess(@Param("uid") String userId, @Param("bid") String boardId);

    @Query(value = "SELECT bp.permission FROM BoardPermission bp WHERE bp.userID = :uid AND bp.boardID = :bid")
    String getPermission(@Param("uid") String userId, @Param("bid") String boardId);
}
