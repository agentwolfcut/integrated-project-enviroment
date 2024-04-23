package dev.backendintegratedproject.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Tasks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "taskTitle", length = 100, nullable = false)
    private String title;

    @Column(name = "taskDescription", length = 500)
    private String description;

    @Column(name = "taskAssignees", length = 30)
    private String assignees;

    @Column(name = "taskStatus")
    private String status;

    @Column(name = "createdOn", nullable = false)
    private Date createdOn = new Date();

    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn = new Date();
}
