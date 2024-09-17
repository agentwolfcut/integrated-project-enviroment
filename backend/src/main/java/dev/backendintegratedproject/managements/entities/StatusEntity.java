package dev.backendintegratedproject.managements.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "status")
public class StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusID")
    private Integer id;

    @Column(name = "statusName", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "statusDescription", length = 200)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<TaskEntity> tasks;

    @JsonIgnore
    public String getStatusName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description == null?null:description.trim().length()==0?null:description.trim();
    }

    public void setName(String name) {

        this.name = name == null?null:name.trim();
    }


}

