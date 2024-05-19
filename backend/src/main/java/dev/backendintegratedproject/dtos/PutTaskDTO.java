package dev.backendintegratedproject.dtos;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class PutTaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String assignees;

    // Only include the name from StatusEntity
    private String status;

    private String createdOn;
    private String updatedOn;
}
