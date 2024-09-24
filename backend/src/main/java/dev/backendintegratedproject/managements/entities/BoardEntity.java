package dev.backendintegratedproject.managements.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board")
public class BoardEntity {

    @Id
    @Column(length = 10, nullable = false , name = "boardId")
    private String boardId;

    @Column(length = 120, nullable = false, name = "name")
    private String boardName;

    @Column(length = 36, nullable = false , name = "owner_id")
    private String ownerId;

    @Column(name = "createdOn", nullable = false)
    private Date createdOn;

    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn;

    @OneToMany(mappedBy = "boardId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusEntity> statuses;

    @OneToMany(mappedBy = "boardId")
    private List<TaskEntity> tasks;

}
