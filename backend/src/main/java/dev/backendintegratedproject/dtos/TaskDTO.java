package dev.backendintegratedproject.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class TaskDTO {
    private Integer id;
    @NotBlank(message = "must not be null")
    @Size(max = 100, message = "Title size must be between 1 and 100")
    private String title;
    @Size(max = 500, message = "Description size must be between 1 and 500")
    private String description;
    @Size(max = 30, message = "Assignees size must be up to 30 characters")
    private String assignees;
    private String status;
}
