package dev.backendintegratedproject.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ExceptionForm {
    private final Integer status;
    private final String message;
    private final String stacktrace;
    private List<ValidationErrors> validationErrors = new ArrayList<>();

    @Getter
    @Setter
    @AllArgsConstructor
    private class ValidationErrors{
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message){
        this.validationErrors.add(new ValidationErrors(field, message));
    }
}
