package dev.backendintegratedproject.dtos.task;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import dev.backendintegratedproject.validations.annotations.StatusExists;

@Data
public class CreateTaskDTO {
    @NotNull(message = "Task title is required")
    @NotEmpty(message = "Task title must not be empty")
    @Size(max = 100, message = "size must be between 0 and 100")
    private String title;
    @Size(max = 500, message = "size must be between 0 and 500")
    private String description;
    @Size(max = 30, message = "size must be between 0 and 30")
    private String assignees;
    @Transient
    @NotNull
    @StatusExists
    private Integer statusId;
}
