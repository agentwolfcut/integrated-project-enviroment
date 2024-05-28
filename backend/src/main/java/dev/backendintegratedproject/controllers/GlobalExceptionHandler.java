package dev.backendintegratedproject.controllers;

import dev.backendintegratedproject.exceptions.ErrorResponse;
import dev.backendintegratedproject.exceptions.ItemNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.",
                request.getDescription(false)
        );

        List<ParameterValidationResult> paramNames = exception.getAllValidationResults();
        for (ParameterValidationResult param : paramNames) {
            errorResponse.addValidationError(
                    param.getMethodParameter().getParameterName(),
                    param.getResolvableErrors().get(0).getDefaultMessage() + " (" + param.getArgument().toString() + ")"
            );
        }

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error. Check 'errors' field for details.",
                request.getDescription(false)
        );

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({ItemNotFoundException.class, ResponseStatusException.class, HttpClientErrorException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(RuntimeException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleJpaException(DataAccessException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception, WebRequest request) {
        return buildErrorResponse(exception, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message, request.getDescription(false));
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
