package dev.backendintegratedproject.dtos.board;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CollaboratorRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "READ|WRITE")
    private String accessRight;
}

