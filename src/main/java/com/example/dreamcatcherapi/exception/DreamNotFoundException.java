// C:\Users\login\OneDrive\Desktop\dreamcatcherapi\src\main\java\com\example\dreamcatcherapi\exception\DreamNotFoundException.java
package com.example.dreamcatcherapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Возвращает 404 Not Found, когда это исключение бросается
public class DreamNotFoundException extends RuntimeException {

    public DreamNotFoundException(String message) {
        super(message);
    }

    public DreamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
