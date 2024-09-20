package dev.backendintegratedproject.managements.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "status")
public class StatusV3Entity extends StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusID")
    private Integer id;

    @Column(name = "statusName", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "statusDescription", length = 200)
    private String description;

    // Relationship to the BoardEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = false)
    private BoardEntity board;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskEntity> tasks;

    @JsonIgnore
    public String getStatusName() {
        return name;
    }

    public void setStatusName(String statusName) {
        this.name = statusName != null && !statusName.trim().isEmpty() ? statusName.trim() : null;
    }

    public String getStatusDescription() {
        return description;
    }

    public void setStatusDescription(String statusDescription) {
        this.description = statusDescription != null && !statusDescription.trim().isEmpty() ? statusDescription.trim() : null;
    }
    @Override
    @JsonIgnore
    public List<TaskEntity> getTasks() {
        return tasks;  // กำหนดให้ TaskV3Entity คืนค่าแทน TaskEntity
    }
    @Override
    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }


}
