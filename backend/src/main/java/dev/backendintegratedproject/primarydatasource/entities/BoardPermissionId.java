package dev.backendintegratedproject.primarydatasource.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BoardPermissionId implements Serializable {

    @Column(name = "userID")
    private String userId;

    @Column(name = "boardID")
    private String boardId;

    public BoardPermissionId() {}

    public BoardPermissionId(String userId, String boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }

    public String getUserId() {
        return userId;
    }

    public String getBoardId() {
        return boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPermissionId that = (BoardPermissionId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(boardId, that.boardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, boardId);
    }
}
