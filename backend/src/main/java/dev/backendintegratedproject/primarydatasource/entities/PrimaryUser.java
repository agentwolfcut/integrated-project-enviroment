package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrimaryUser {

    @Id
    @Column(name = "userID", nullable = false)
    private String oid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = true) // อนุญาตให้ email เป็น null ได้
    private String email;

    // เพิ่มความสัมพันธ์กับ BoardPermission
    @OneToMany(mappedBy = "primaryUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardPermission> permissions;

    public PrimaryUser(String oid, String username) {
        this.oid = oid;
        this.username = username;
    }
}

