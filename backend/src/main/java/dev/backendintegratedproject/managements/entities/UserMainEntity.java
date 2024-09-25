package dev.backendintegratedproject.managements.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMainEntity {
    @Id
    @Column(name = "oid", unique = true, nullable = false)
    private String oid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "created_on", updatable = false, insertable = false)
    private Date createdOn;

    @Column(name = "updated_on", updatable = false, insertable = false)
    private Date updatedOn;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<BoardEntity> boards;

}
