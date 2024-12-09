package dev.backendintegratedproject.dtos.board;

import dev.backendintegratedproject.primarydatasource.entities.AccessRight;

public class AccessRightDTO {
    private String accessRight;

    public AccessRight getAccessRight() {
        return AccessRight.valueOf(accessRight);
    }
}
