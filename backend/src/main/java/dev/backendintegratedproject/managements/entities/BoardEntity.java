package dev.backendintegratedproject.managements.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board")
public class BoardEntity {

    @Id
    @Column(name = "boardID", length = 10, nullable = false, unique = true)
    private String boardId;

    @Column(name = "name", length = 120, nullable = false)
    private String boardName;

    @Column(name = "owner_id", length = 36, nullable = false)
    private String ownerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdOn", nullable = false, updatable = false)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusV3Entity> statuses;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskV3Entity> tasks;
}