package dev.backendintegratedproject.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusDTO {
    private Integer statusID;
    private String statusName;
    private String statusDescription;
    public StatusDTO() {
    }
}

