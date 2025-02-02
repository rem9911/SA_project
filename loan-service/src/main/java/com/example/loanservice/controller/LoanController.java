package com.example.loanservice.controller;

import com.example.loanservice.exceptions.LoanAlreadyExistsException;
import com.example.loanservice.exceptions.LoanNotFoundException;
import com.example.loanservice.model.Loan;
import com.example.loanservice.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Retrieve all loans")
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {

        return ResponseEntity.ok().body(loanService.getAllLoans());
    }

    @Operation(summary = "Retrieve a loan by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable Long id) {
        try {
            Loan loan = loanService.getLoanById(id);
            if (loan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(loan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Create a new loan")
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {
        System.out.println("Received Loan request: " + loan);

        try {
            Loan response = loanService.createLoan(loan);
            System.out.println("Saved Loan: " + response);
            return ResponseEntity.ok().body(response);
        } catch (LoanAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @Operation(summary = "Update an existing loan")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        try {
            Loan response = loanService.updateLoan(id, loan);
            return ResponseEntity.ok().body(response);
        } catch (LoanNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete a loan by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        try {
            boolean deleted = loanService.deleteLoan(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
