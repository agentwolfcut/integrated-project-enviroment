package dev.backendintegratedproject.primarydatasource.repositories;

import dev.backendintegratedproject.primarydatasource.entities.Collaborators;
import dev.backendintegratedproject.primarydatasource.entities.CollaboratorsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollaboratorsRepository extends JpaRepository<Collaborators, CollaboratorsId> {

    @Query("SELECT COUNT(cb) > 0 FROM Collaborators cb WHERE cb.userOid = :uid AND cb.boardID = :bid")
    Boolean checkBoardAccess(@Param("uid") String userId, @Param("bid") String boardId);

    @Query("SELECT cb.accessRight FROM Collaborators cb WHERE cb.userOid = :uid AND cb.boardID = :bid")
    String getAccessRight(@Param("uid") String userId, @Param("bid") String boardId);

    // ลบ Collaborator
    void deleteByUserOidAndBoardID(String userOid, String boardID);

    // ค้นหา Collaborator
    Optional<Collaborators> findByUserOidAndBoardID(String userOid, String boardID);

    // ค้นหา Collaborators ทั้งหมดในบอร์ด
    List<Collaborators> findAllByBoardID(String boardID);

    boolean existsByUserOidAndBoardID(String userOid, String boardID);

    Optional<Collaborators> findByBoardIDAndUserOid(String boardID, String userOid);

    List<Collaborators> findAllByUserOid(String userOid);

}
