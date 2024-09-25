package dev.backendintegratedproject.dtos;

<<<<<<< Updated upstream
import java.util.Date;
=======
import dev.backendintegratedproject.managements.entities.UserMainEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> Stashed changes

public class BoardDTO {

<<<<<<< Updated upstream
    private String boardId;
    private String boardName;
    private String ownerId;
    private Date createdOn;
    private Date updatedOn;
=======
    private String id;
    @NotBlank(message = "Board name must not be blank")
    private String name;
    private UserMainEntity owner;
>>>>>>> Stashed changes


    public String getName() {
        return boardName;
    }
}
