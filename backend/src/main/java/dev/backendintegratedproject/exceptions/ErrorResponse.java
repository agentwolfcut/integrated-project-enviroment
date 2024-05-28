package dev.backendintegratedproject.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String message;
    private final String instance;
    private List<ValidationError> errors;

    public ErrorResponse(int status, String message, String instance) {
        this.status = status;
        this.message = message;
        this.instance = instance;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }

    @Getter
    @Setter
    private static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
