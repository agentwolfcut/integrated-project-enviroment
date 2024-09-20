package dev.backendintegratedproject.managements.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task")
public class TaskV3Entity extends TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID", nullable = false)
    private Long taskID;

    @Column(name = "taskTitle", nullable = false, length = 100)
    private String taskTitle;

    @Column(name = "taskDescription", length = 500)
    private String taskDescription;

    @Column(name = "taskAssignees", length = 30)
    private String taskAssignees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusID", nullable = false)
    private StatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = false)
    private BoardEntity board;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdOn", nullable = false, updatable = false)
    private Date createdOn = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusID", nullable = false)
    private StatusV3Entity statusV3Entity;  // สืบทอดจาก StatusV3Entity แทน


    @Override
    public StatusV3Entity getStatus() {
        return (StatusV3Entity) status;
    }

    @Override
    public void setStatus(StatusEntity status) {
        this.status = (StatusV3Entity) status;  // กำหนดให้เป็น StatusV3Entity
    }
    @PreUpdate
    public void preUpdate() {
        updatedOn = new Date();
    }

    public StatusEntity getStatusId() {
        return status;
    }

    public void setBoardId(BoardEntity board) {

    }

    public void setStatusId(StatusEntity status) {
    }
}
