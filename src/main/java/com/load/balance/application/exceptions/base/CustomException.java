package com.load.balance.application.exceptions.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String error;

    public CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.error = "GENERIC_ERROR";
        this.status = httpStatus;
    }

    public CustomException(HttpStatus httpStatus, String message, String error) {
        super(message);
        this.status = httpStatus;
        this.error = error;
    }


}
