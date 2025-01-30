package com.bank.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomError.class)
    @ResponseBody
    public void handleCustomError(CustomError ex) {
        if (ex.getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED) {
            throw ex;
        }
        throw ex;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("JWT Token has expired. Please log in again.");
    }

}