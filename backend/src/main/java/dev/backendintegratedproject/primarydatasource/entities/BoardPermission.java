package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPermission {

    @EmbeddedId
    private BoardPermissionId id;

    @Column(name = "permission", nullable = false)
    private String permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userID", insertable = false, updatable = false)
    private PrimaryUser primaryUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardId")
    @JoinColumn(name = "boardID", insertable = false, updatable = false)
    private Board board;

    // เพิ่มฟิลด์ addedOn ตรงนี้ให้ตรงกับฐานข้อมูล
    @Column(name = "addedOn", nullable = false, updatable = false)
    private LocalDateTime addedOn = LocalDateTime.now();
}
