package dev.backendintegratedproject.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class TaskDTO {
    private Integer id;

    @NotNull(message = "Title must not be null")
    @NotBlank(message = "Title must not be blank")
    private String title;
    private String description;
    @Size(max = 30, message = "Size must be between 0 and 30")
    private String assignees;
    @NotNull(message = "Status must not be null")
    private String status;
}
