package com.load.balance.application.exceptions.handler;

import com.load.balance.application.exceptions.base.CustomException;
import com.load.balance.application.exceptions.base.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(),
                ex.getError(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }
}
