package com.aea.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidatonErrorResponse> handleInvalidRequestArgs(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidatonErrorResponse response =
                new ValidatonErrorResponse("Please provide the required fields", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<String> handleNoHandlerFoundException(Exception ex) {
        return new ResponseEntity<>("Path not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabledUserException(DisabledException ex) {
        return new ResponseEntity<>("Your account is disabled. Please verify your email", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String errorMessage = "Something went wrong on our side. We will work on it!";
        System.out.println("----- ERROR -----");
        System.out.println(ex);
        System.out.println("-----------------");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
