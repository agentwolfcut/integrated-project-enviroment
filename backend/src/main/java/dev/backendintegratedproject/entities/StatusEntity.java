package dev.backendintegratedproject.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Status")
public class StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusID")
    private Integer id;

    @NotBlank(message = "Status name must not be null")
    @Size(max = 50, message = "Status name size must be between 1 and 50")
    @Column(name = "statusName", nullable = false, unique = true, length = 50)
    private String name;

    @Size(max = 200, message = "Description size must be between 0 and 200")
    @Column(name = "statusDescription", length = 200)
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<TaskEntity> tasks;

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

