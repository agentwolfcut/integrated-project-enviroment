package dev.backendintegratedproject.dtos.board;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class BoardDTO {
    private String id;
    private String name;
    private String visibility;
    private String ownerID;

    public void setCollaborators(ArrayList<Object> objects) {

    }

}
