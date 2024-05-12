package com.example.carrental.exceptions;

public class DateNotValidException extends RuntimeException {
    public DateNotValidException(String message) {
        super(message);
    }
}
