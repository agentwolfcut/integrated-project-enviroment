package dev.backendintegratedproject.dtos;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private String status;
    private String createdOn;
    private String updatedOn;
}
