package dev.backendintegratedproject.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "statusDescription", nullable = false, length = 200)
    private String description;


}

