package dev.backendintegratedproject.managements.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
@Getter
@Setter
@Entity
@Table(name = "Task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID")
    private Integer id;


    @Column(name = "taskTitle", length = 100, nullable = false)
    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;


    @Column(name = "taskDescription", length = 500)
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;


    @Column(name = "taskAssignees", length = 30)
    @Size(max = 30, message = "Assignees must be less than 30 characters")
    private String assignees;


    @ManyToOne
    @JoinColumn(name = "statusID")
    private StatusEntity status;

    @Column(name = "createdOn", nullable = false)
    private Date createdOn;

    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn;

    @ManyToOne
    @JoinColumn(referencedColumnName = "statusId" ,name ="statusId")
    private StatusEntity statusId ;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "boardId", nullable = false)
    private BoardEntity boardId;


    public void setDescription(String description) {
        this.description = description == null?null:description.trim().length()==0?null:description.trim();
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees == null?null:assignees.trim().length()==0?null:assignees.trim();
    }
    public void setTitle(String title){
        this.title = title == null || title.trim().isEmpty()?null:title.trim();
    }





    private String dateformat(Date date_s) throws ParseException {
        if (date_s ==null) date_s = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");
        dt.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dt1.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dt1.format(dt.parse(dt.format(date_s)));
    }

    public Object getTaskId() {
        return id;
    }
}