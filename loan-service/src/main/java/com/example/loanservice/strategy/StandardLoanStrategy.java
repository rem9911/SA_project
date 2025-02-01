package com.example.loanservice.strategy;

import com.example.loanservice.exceptions.BookServiceException;
import com.example.loanservice.exceptions.LoanAlreadyExistsException;
import com.example.loanservice.kafka.LoanProducer;
import com.example.loanservice.model.Loan;
import com.example.loanservice.repository.LoanRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class StandardLoanStrategy implements LoanStrategy {


    private final LoanRepository loanRepository;
    private final RestTemplate restTemplate;
    private final LoanProducer loanProducer;

    public StandardLoanStrategy(LoanRepository loanRepository, LoanProducer loanProducer) {
        this.loanRepository = loanRepository;
        this.loanProducer = loanProducer;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Loan createLoan(Loan loan) throws LoanAlreadyExistsException, BookServiceException {
        Optional<Loan> existingLoan = loanRepository.findByBookId(loan.getBookId());
        if (existingLoan.isPresent()) {
            throw new LoanAlreadyExistsException("This book is already loaned to the user.");
        }

        String bookServiceUrl = "http://localhost:8080/api/books/" + loan.getBookId() + "/availability?available=false";
        try {
            restTemplate.put(bookServiceUrl, null);
        } catch (Exception e) {
            throw new BookServiceException("Failed to update book availability: " + e.getMessage());
        }

        loan.setReturnDate(loan.getLoanDate().plusDays(60));
        Loan savedLoan = loanRepository.save(loan);
        loanProducer.sendLoanEvent("Loan Created: " + savedLoan);

        return savedLoan;
    }
}

