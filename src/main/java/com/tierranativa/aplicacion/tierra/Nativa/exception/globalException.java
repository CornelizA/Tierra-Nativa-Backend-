package com.tierranativa.aplicacion.tierra.nativa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class globalException {

    @ExceptionHandler(resourceNotFoundException.class)
    public ResponseEntity<String> processResourceNotFoundException(resourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(resourceAlreadyExistsException.class)
    public ResponseEntity<String> processResourceAlreadyExistsException(resourceAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
