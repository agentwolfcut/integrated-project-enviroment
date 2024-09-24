package dev.backendintegratedproject.dtos;

import dev.backendintegratedproject.managements.entities.UserMainEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private String id;
    private String name;
    private UserMainEntity owner;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMainEntity {
        private String oid;
        private String name;
    }


}
