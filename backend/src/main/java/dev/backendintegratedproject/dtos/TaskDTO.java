package dev.backendintegratedproject.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private String status;
}
