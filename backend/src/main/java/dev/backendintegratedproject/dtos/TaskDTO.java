package dev.backendintegratedproject.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String assignees;
    private String status;
    private Date createdOn;
    private Date updatedOn;
}
