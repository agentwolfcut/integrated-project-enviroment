package dev.backendintegratedproject.dtos.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    private String id;
    private String name;
    private String visibility;
    private String ownerID;
}

