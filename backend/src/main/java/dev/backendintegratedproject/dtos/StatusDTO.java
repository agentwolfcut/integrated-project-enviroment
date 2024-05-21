package dev.backendintegratedproject.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StatusDTO {
    private Integer id;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    @Size(max = 50, message = "Size must be between 0 and 50")
    
    private String name;
    private String description;
}

