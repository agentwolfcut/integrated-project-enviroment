package dev.backendintegratedproject.entities;

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
@Table(name = "Tasks_v2")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID")
    private Integer id;

    @NotBlank(message = "Title must not be null")
    @Size(max = 100, message = "Title size must be between 1 and 100")
    @Column(name = "taskTitle", length = 100, nullable = false)
    private String title;

    @Size(max = 500, message = "Description size must be between 0 and 500")
    @Column(name = "taskDescription", length = 500)
    private String description;

    @Size(max = 30, message = "Assignees size must be between 0 and 30")
    @Column(name = "taskAssignees", length = 30)
    private String assignees;

    @NotNull(message = "Status must not be null")
    @ManyToOne
    @JoinColumn(name = "statusID")
    private StatusEntity status;

    @Column(name = "createdOn", nullable = false)
    private Date createdOn;

    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn;


    public void setDescription(String description) {
        this.description = description == null?null:description.trim().length()==0?null:description.trim();
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees == null?null:assignees.trim().length()==0?null:assignees.trim();
    }

    public void setTitle(String title) {
        this.title = title == null?null:title.trim();
    }



    public String getCreatedOn() throws ParseException {
        return dateformat(createdOn);
    }

    public String getUpdatedOn() throws ParseException {
        return dateformat(updatedOn);
    }
    private String dateformat(Date date_s) throws ParseException {
        if (date_s ==null) date_s = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");
        dt.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dt1.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dt1.format(dt.parse(dt.format(date_s)));
    }
}