package dev.backendintegratedproject.dtos.board;

import dev.backendintegratedproject.primarydatasource.entities.AccessRight;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateAccessRightDTO {

    private String accessRight;

    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }
}



