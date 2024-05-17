package dev.backendintegratedproject.dtos;
import lombok.Data;
@Data
public class GetTaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private String status;
}
