package dev.backendintegratedproject.dtos.board;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisibilityDTO {
    @NotNull(message = "visibility is required")
    private String visibility;
}
