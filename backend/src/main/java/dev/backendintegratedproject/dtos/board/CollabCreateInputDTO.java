package dev.backendintegratedproject.dtos.board;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollabCreateInputDTO {
    @Size(min = 0, max = 50)
    private String email;
    private String accessRight;
}
