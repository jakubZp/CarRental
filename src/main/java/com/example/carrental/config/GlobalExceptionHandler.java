package com.example.carrental.config;

import com.example.carrental.exceptions.CannotFindPriceException;
import com.example.carrental.exceptions.DateNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DateNotValidException.class, CannotFindPriceException.class})
    public Map<String, String> handleDateException(RuntimeException dateException) {
        Map <String, String> error = new HashMap<>();
        String fieldName = "error";
        String message = dateException.getMessage();
        error.put(fieldName, message);
        return error;
    }
}
