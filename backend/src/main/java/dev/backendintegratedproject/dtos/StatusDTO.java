package dev.backendintegratedproject.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StatusDTO {
    private Integer id;
    private String name;
    private String description;
}

