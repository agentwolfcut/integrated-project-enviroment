package dev.backendintegratedproject.dtos;


import lombok.Data;



@Data
public class PutTaskDTO {

    private Integer id;

    private String title;

    private String description;

    private String assignees;

    private String status;

    private String createdOn;

    private String updatedOn;




}
