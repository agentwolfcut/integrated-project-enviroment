package dev.backendintegratedproject.dtos.board;

import dev.backendintegratedproject.primarydatasource.entities.AccessRight;
import lombok.Data;

@Data
public class CollabOutputDTO {
    private String oid;
    private String name;
    private String email;
    private AccessRight accessRight;
    private String AddedOn;
}
