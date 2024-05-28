package dev.backendintegratedproject.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class StatusDTO {
    private Integer id;
    @NotBlank(message = "must not be null")
    @Size(max = 50, message = "Status name cannot more than 50 character")
    private String name;
    @Size(max = 200, message = "Status description cannot more than 200 character")
    private String description;
}

