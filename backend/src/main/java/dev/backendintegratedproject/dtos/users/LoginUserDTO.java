package dev.backendintegratedproject.dtos.users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDTO {
    @NotEmpty
    @Size(max = 50)
    private String username;
    @NotEmpty
    @Size(max = 14)
    private String password;
}
