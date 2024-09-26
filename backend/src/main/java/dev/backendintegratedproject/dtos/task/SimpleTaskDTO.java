package dev.backendintegratedproject.dtos.task;

import lombok.Data;
import dev.backendintegratedproject.primarydatasource.entities.Status;

@Data
public class SimpleTaskDTO {
    private Integer id;
    private String Title;
    private String Assignees;
    private Status Status;

//    public String getStatus(){
//        return Status.getName();
//    }
}
