package dev.backendintegratedproject.dtos.task;

import lombok.Data;
import dev.backendintegratedproject.primarydatasource.entities.Status;

import java.time.ZonedDateTime;

@Data
public class DetailedTaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private Status status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
