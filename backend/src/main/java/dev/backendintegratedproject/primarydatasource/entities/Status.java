package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "statuses")
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusID", nullable = false, unique = true)
    private Integer id;
    @Column(name = "statusName", nullable = false, unique = true, length = 50)
    private String name;
    @Column(name = "statusDescription", nullable = true, length = 200)
    private String description;
    @Column(name = "boardID")
    private String boardID;

//    @ManyToOne
//    @JoinColumn(name = "boardID" , insertable = false, updatable = false)
//    private Board board;


}
