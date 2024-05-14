package dev.backendintegratedproject.entities;
import jakarta.persistence.*;
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

    @Column(name = "statusName", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "statusDescription", length = 200)
    private String description;

    @OneToMany(mappedBy = "status")
    private List<TaskEntity> tasks;
    public String getStatusName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description == null?null:description.trim();
    }
    public void setName(String name) {

        this.name = name == null?null:name.trim();
    }

//    public void setName(String name) {
//        this.name = name == null ? null : name.trim().toUpperCase().replace(" ", "_");
//    }
//    public String getStatus(){
//
//        return name == null?"No Status":name.trim().toUpperCase().replace(" ","_");
//    }


}

