package com.example.loanservice.strategy;

import com.example.loanservice.exceptions.BookServiceException;
import com.example.loanservice.exceptions.LoanAlreadyExistsException;
import com.example.loanservice.model.Loan;

public interface LoanStrategy {
    Loan createLoan(Loan loan) throws LoanAlreadyExistsException, BookServiceException;
}

