package dev.backendintegratedproject.dtos.board;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBoardDTO {
    @NotNull(message = "Board name is required")
    @Size(min = 1, max = 120, message = "size must be between 1 and 120")
    private String boardName;
}
