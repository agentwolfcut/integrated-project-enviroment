package dev.backendintegratedproject.primarydatasource.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CollaboratorsId implements Serializable {
    @Column(name = "userOid", nullable = false)
    private String userOid;

    @Column(name = "boardID", nullable = false)
    private String boardID;
}
