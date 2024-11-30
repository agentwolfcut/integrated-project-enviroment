package dev.backendintegratedproject.primarydatasource.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.internal.bytebuddy.description.modifier.Visibility;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @Column(name = "boardID", length = 10)
    private String id;

    @Column(name = "boardName", nullable = false, length = 45)
    private String name;

    @Column(name = "ownerID", nullable = false, length = 36)
    private String ownerID;

    @JsonIgnore
    @Column(name = "isPublic", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean visibility;



    @JsonIgnore
    public void setVisibility(String visibility) {
        if ("PUBLIC".equalsIgnoreCase(visibility)) {
            this.visibility = true;
        } else if ("PRIVATE".equalsIgnoreCase(visibility)) {
            this.visibility = false;
        } else {
            throw new IllegalArgumentException("Invalid visibility value. Only 'PUBLIC' or 'PRIVATE' are allowed.");
        }
    }

    @ManyToOne
    @JoinColumn(name = "ownerID", insertable = false, updatable = false, referencedColumnName = "userID", nullable = false)
    private PrimaryUser owner;

    @PrePersist
    public void generateBoardID() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 10);
        }
    }

    public boolean isPublic() {
        return Boolean.TRUE.equals(this.visibility);
    }
}
