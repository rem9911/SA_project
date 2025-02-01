package com.example.loanservice.service;


import com.example.loanservice.kafka.LoanProducer;
import com.example.loanservice.model.Loan;
import com.example.loanservice.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanProducer loanProducer;

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }


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
