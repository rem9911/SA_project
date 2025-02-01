package com.example.loanservice.exceptions;

public class LoanAlreadyExistsException extends Exception {
    public LoanAlreadyExistsException(String message) {
        super(message);
    }
}

