package com.example.loanservice.controller;

import com.example.loanservice.kafka.LoanProducer;
import com.example.loanservice.model.Loan;
import com.example.loanservice.repository.LoanRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanRepository loanRepository;
    private final LoanProducer loanProducer;

    public LoanController(LoanRepository loanRepository, LoanProducer loanProducer) {
        this.loanRepository = loanRepository;
        this.loanProducer = loanProducer;
    }

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @PostMapping
    public Loan createLoan(@RequestBody Loan loan) {
        // Assure-toi que l'objet reçu est bien traité avant de l'enregistrer
        Loan savedLoan = loanRepository.save(loan);

        String bookServiceUrl = "http://localhost:8080/api/books/" + loan.getBookId() + "/availability?available=false";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(bookServiceUrl, null);

        loanProducer.sendLoanEvent("Loan created for book ID: " + loan.getBookId());

        return savedLoan;
    }

}
