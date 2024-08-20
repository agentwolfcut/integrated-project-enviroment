package dev.backendintegratedproject.userManage;



import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class LoginRequest {
    @NotEmpty(message = "Username cannot be empty")
    @Size(max = 50, message = "Username must be at most 50 characters long")
    private String userName;
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 14, message = "Password must be at most 14 characters long")
    private String password;
}
