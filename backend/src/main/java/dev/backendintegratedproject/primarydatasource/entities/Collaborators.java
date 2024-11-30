package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "collaborators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CollaboratorsId.class) // ใช้ Composite Key
public class Collaborators {

    @Id
    @Column(name = "userOid", nullable = false)
    private String userOid;

    @Id
    @Column(name = "boardID", nullable = false)
    private String boardID;

    @Enumerated(EnumType.STRING)
    @Column(name = "accessRight", nullable = false)
    private AccessRight accessRight = AccessRight.READ;


    @Column(name = "addedOn", insertable = false, updatable = false)
    private ZonedDateTime addedOn;

    @ManyToOne
    @JoinColumn(name = "boardID", insertable = false, updatable = false)
    private Board board;


    public Object getStatus() {
        return null;
    }
}
