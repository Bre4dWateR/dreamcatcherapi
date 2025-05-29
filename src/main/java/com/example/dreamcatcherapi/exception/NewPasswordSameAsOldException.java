package com.example.dreamcatcherapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Возвращает HTTP 409
public class NewPasswordSameAsOldException extends RuntimeException {
    public NewPasswordSameAsOldException(String message) {
        super(message);
    }
}
