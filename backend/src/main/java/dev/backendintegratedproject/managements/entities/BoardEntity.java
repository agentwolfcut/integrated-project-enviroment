package dev.backendintegratedproject.managements.entities;

import dev.backendintegratedproject.userManage.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "boards")
public class BoardEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserMainEntity owner;


    @Column(name = "created_on", updatable = false, insertable = false)
    private Date createdOn;

    @Column(name = "updated_on", updatable = false, insertable = false)
    private Date updatedOn;


}