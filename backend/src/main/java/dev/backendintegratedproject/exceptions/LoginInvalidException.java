package dev.backendintegratedproject.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
@AllArgsConstructor
public class LoginInvalidException extends Exception{
    public LoginInvalidException(String message) {
        super(message);
    }
}
