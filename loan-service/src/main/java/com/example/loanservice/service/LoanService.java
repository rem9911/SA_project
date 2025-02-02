package com.example.loanservice.service;


import com.example.loanservice.exceptions.BookServiceException;
import com.example.loanservice.exceptions.LoanAlreadyExistsException;
import com.example.loanservice.exceptions.LoanNotFoundException;
import com.example.loanservice.kafka.LoanProducer;
import com.example.loanservice.model.Loan;
import com.example.loanservice.model.LoanType;
import com.example.loanservice.repository.LoanRepository;
import com.example.loanservice.strategy.LoanStrategy;
import com.example.loanservice.strategy.PremiumLoanStrategy;
import com.example.loanservice.strategy.StandardLoanStrategy;
import com.example.loanservice.strategy.StudentLoanStrategy;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanProducer loanProducer;
    private final StandardLoanStrategy standardLoanStrategy;
    private final PremiumLoanStrategy premiumLoanStrategy;
    private final StudentLoanStrategy studentLoanStrategy;


    public LoanService(LoanRepository loanRepository, LoanProducer loanProducer, StandardLoanStrategy standardLoanStrategy, PremiumLoanStrategy premiumLoanStrategy, StudentLoanStrategy studentLoanStrategy) {
        this.loanRepository = loanRepository;
        this.loanProducer = loanProducer;
        this.standardLoanStrategy = standardLoanStrategy;
        this.premiumLoanStrategy = premiumLoanStrategy;
        this.studentLoanStrategy = studentLoanStrategy;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        System.out.println("Loans found: " + loans.size());
        loans.forEach(loan -> System.out.println("Loan: " + loan));
        return loans;
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }


    public Loan createLoan(Loan loan) throws LoanAlreadyExistsException, BookServiceException {
        LoanStrategy strategy = selectStrategy(loan.getLoanType());

        // Ensure return date is set by the strategy
        Loan createdLoan = strategy.createLoan(loan);

        // Save the loan with the calculated return date
        return loanRepository.save(createdLoan);
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

    private LoanStrategy selectStrategy(LoanType loanType) {
        return switch (loanType) {
            case PREMIUM -> premiumLoanStrategy;
            case STUDENT -> studentLoanStrategy;
            default -> standardLoanStrategy;
        };
    }
}
