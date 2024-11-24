package dev.backendintegratedproject.dtos.board;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CollaboratorRequest {
    @Email(message = "Invalid email format")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "AccessRight is required")
    @Pattern(regexp = "READ|WRITE", message = "AccessRight must be READ or WRITE")
    private String accessRight;

    public String getUserID() {
        return email;
    }

    public String getPermission() {
        return accessRight;
    }
}

