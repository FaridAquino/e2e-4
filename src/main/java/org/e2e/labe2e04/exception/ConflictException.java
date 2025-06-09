package org.e2e.labe2e04.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Conflict occurred")
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
