package com.tierranativa.aplicacion.tierra.nativa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class resourceAlreadyExistsException extends RuntimeException {
    public resourceAlreadyExistsException(String message) {
        super(message);
    }
}
