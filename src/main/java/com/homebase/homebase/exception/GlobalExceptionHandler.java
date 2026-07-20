package com.homebase.homebase.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.homebase.homebase.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException (
            ResourceNotFoundException exception,
            HttpServletRequest request,
            Authentication authentication
    ) {
        LocalDateTime now = LocalDateTime.now();
        log.warn(
                "Resource not found: {} | name={} | path={}",
                exception.getMessage(),
                authentication.getName(),
                request.getRequestURI()
        );
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                now
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException (
            DuplicateResourceException exception,
            HttpServletRequest request,
            Authentication authentication
    ) {
        LocalDateTime now = LocalDateTime.now();
        log.warn(
                "Duplicate Resource : {} | name={} | path={}",
                exception.getMessage(),
                authentication.getName(),
                request.getRequestURI()
        );
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.CONFLICT,
                now
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
