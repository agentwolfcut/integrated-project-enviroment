package dev.backendintegratedproject.dtos.board;

import lombok.Data;

@Data
public class CollaboratorRequestDTO {
    private String email;
    private String accessRight;
}
