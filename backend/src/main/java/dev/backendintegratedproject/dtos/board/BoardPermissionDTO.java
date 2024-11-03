package dev.backendintegratedproject.dtos.board;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BoardPermissionDTO {
    private String userId;
    private String userName;
    private String email;
    private String permission;
    private LocalDateTime addedOn;

    public BoardPermissionDTO(String userId, String userName, String email, String permission, LocalDateTime addedOn) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.permission = permission;
        this.addedOn = addedOn;
    }

    public BoardPermissionDTO() {}
}
