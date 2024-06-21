package com.example.carrental.exceptions;

public class CannotFindPriceException extends RuntimeException{
    public CannotFindPriceException(String message) {
        super(message);
    }
}
