package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "board_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPermission {
    @Id
    @Column(name = "userOid")
    private String userID;
    @Column(name = "boardID")
    private String boardID;
    @Column(name = "permission", nullable = false)
    private String permission = "user";

}
