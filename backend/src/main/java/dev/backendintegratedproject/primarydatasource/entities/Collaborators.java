package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Table(name = "collaborators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CollaboratorsId.class) // ใช้ Composite Key
public class Collaborators {

    @Id
    @Column(name = "userOid", nullable = false)
    private String userOid;

    @Id
    @Column(name = "boardID", nullable = false)
    private String boardID;

    @Enumerated(EnumType.STRING)
    @Column(name = "accessRight", nullable = false)
    private AccessRight accessRight = AccessRight.READ;


    @Column(name = "addedOn", insertable = false, updatable = false)
    private Date addedOn;

    @ManyToOne
    @JoinColumn(name = "boardID", insertable = false, updatable = false)
    private Board board;


    public Object getStatus() {
        return null;
    }
    private String getDateString(Date d) throws ParseException {
        if (d == null) d = new Date();
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");
        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        in.setTimeZone(TimeZone.getTimeZone("UTC"));
        out.setTimeZone(TimeZone.getTimeZone("UTC"));
        return out.format(in.parse(in.format(d)));
    }

    public String getAddedOn() throws ParseException {
        return getDateString(addedOn);
    }


}
