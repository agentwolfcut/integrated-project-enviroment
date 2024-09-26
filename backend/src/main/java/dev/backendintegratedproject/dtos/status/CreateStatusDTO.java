package dev.backendintegratedproject.dtos.status;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStatusDTO {
    @NotNull(message = "Status name is required")
    @Size(max = 50, message = "size must be between 0 and 50")
    private String name;
    @Size(max = 200, message = "size must be between 0 and 200")
    private String description;

}
