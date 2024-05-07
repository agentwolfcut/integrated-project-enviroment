package dev.backendintegratedproject.entities;
import jakarta.persistence.*;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
@Getter
@Setter
@Entity
@Table(name = "tasks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID")
    private Integer id;

    @Column(name = "taskTitle", length = 100, nullable = false)
    private String title;
    @Column(name = "taskDescription", length = 500)
    private String description;
    @Column(name = "taskAssignees", length = 30)
    private String assignees;
    @Column(name = "taskStatus")
    private String status;
    @Column(name = "createdOn", nullable = false)
    private Date createdOn;
    @Column(name = "updatedOn", nullable = false)
    private Date updatedOn;

    public void setStatus(String status) {
        this.status = status == null?"NO_STATUS":status.trim().toUpperCase().replace(" ","_");
    }
    public String getStatus(){
        return status == null?"NO_STATUS":status.trim().toUpperCase().replace(" ","_");
    }

    public void setDescription(String description) {
        this.description = description == null?null:description.trim();
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees == null?null:assignees.trim();
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
