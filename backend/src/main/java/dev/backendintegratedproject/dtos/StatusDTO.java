package dev.backendintegratedproject.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusDTO {
    private Integer id;
    private String name;
    private String description;
    public StatusDTO() {
    }
}
