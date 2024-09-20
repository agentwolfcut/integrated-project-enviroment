package dev.backendintegratedproject.dtos;

import java.util.Date;

public class BoardDTO {

    private String boardId;
    private String boardName;
    private String ownerId;
    private Date createdOn;
    private Date updatedOn;


    public String getName() {
        return boardName;
    }
}
