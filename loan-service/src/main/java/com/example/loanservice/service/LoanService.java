package com.example.loanservice.service;


import com.example.loanservice.exceptions.BookServiceException;
import com.example.loanservice.exceptions.LoanAlreadyExistsException;
import com.example.loanservice.exceptions.LoanNotFoundException;
import com.example.loanservice.kafka.LoanProducer;
import com.example.loanservice.model.Loan;
import com.example.loanservice.repository.LoanRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanProducer loanProducer;
    private final RestTemplate restTemplate;


    public LoanService(LoanRepository loanRepository, LoanProducer loanProducer) {
        this.loanRepository = loanRepository;
        this.loanProducer = loanProducer;
        this.restTemplate = new RestTemplate();
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }


    public Loan createLoan(Loan loan) throws LoanAlreadyExistsException, BookServiceException {
        Optional<Loan> existingLoan = loanRepository.findByBookId(loan.getBookId() );
        if (existingLoan.isPresent()) {
            throw new LoanAlreadyExistsException("This book is already loaned to the user.");
        }
        Loan savedLoan = loanRepository.save(loan);

        String bookServiceUrl = "http://localhost:8080/api/books/" + loan.getBookId() + "/availability?available=false";
        try {
            restTemplate.put(bookServiceUrl, null);
        } catch (Exception e) {
            throw new BookServiceException("Failed to update book availability: " + e.getMessage());
        }
        loanProducer.sendLoanEvent("Loan Created: " + savedLoan);
        return savedLoan;
    }


    public Loan updateLoan(Long id, Loan updatedLoan) throws LoanNotFoundException {
        Loan existingLoan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        existingLoan.setLoanDate(updatedLoan.getLoanDate());
        existingLoan.setReturnDate(updatedLoan.getReturnDate());
        Loan savedLoan = loanRepository.save(existingLoan);

        loanProducer.sendLoanEvent("Loan Updated: " + savedLoan);
        return savedLoan;
    }

    public boolean deleteLoan(Long id) {
        if (loanRepository.existsById(id)) {
            loanRepository.deleteById(id);
            loanProducer.sendLoanEvent("Loan Deleted: ID " + id);
            return true;
        }
        return false;
    }

    private void sendLoanMessage(String message) {
        loanProducer.sendLoanEvent(message);
    }
}
