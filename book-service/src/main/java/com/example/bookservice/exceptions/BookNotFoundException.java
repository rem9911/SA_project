package com.example.bookservice.exceptions;


public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
