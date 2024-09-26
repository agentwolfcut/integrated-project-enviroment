package dev.backendintegratedproject.exceptions;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionForm> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest wq) {
        ExceptionForm errorForm = new ExceptionForm(HttpStatus.BAD_REQUEST.value(), "Error: validation fields error", wq.getDescription(false));
        for (FieldError fe: ex.getFieldErrors()) {
            errorForm.addValidationError(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorForm);
    }

    @ExceptionHandler(LoginInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionForm> handleLoginException(LoginInvalidException ex, WebRequest wq) {
        ExceptionForm exceptionForm = new ExceptionForm(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), wq.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionForm);
    }



}
