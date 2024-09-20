package dev.backendintegratedproject.managements.entities;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID")
    private Long taskID;

    @Column(name = "taskTitle", length = 100, nullable = false)
    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String taskTitle;

    @Column(name = "taskDescription", length = 500)
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String taskDescription;

    @Column(name = "taskAssignees", length = 30)
    @Size(max = 30, message = "Assignees must be less than 30 characters")
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

    @PreUpdate
    public void preUpdate() {
        updatedOn = new Date();
    }

    public void setDescription(String description) {
        this.taskDescription = description == null ? null : description.trim().isEmpty() ? null : description.trim();
    }

    public void setAssignees(String assignees) {
        this.taskAssignees = assignees == null ? null : assignees.trim().isEmpty() ? null : assignees.trim();
    }

    public void setTitle(String title) {
        this.taskTitle = title == null || title.trim().isEmpty() ? null : title.trim();
    }
}
