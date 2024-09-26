package dev.backendintegratedproject.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
@AllArgsConstructor
public class InvalidTokenException extends Exception {
    public InvalidTokenException(String message) {
        super(message);
    }
}

