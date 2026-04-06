package com.example.loanmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.repository.LoanRepository;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public Loan createLoan(Loan loan) {
        loan.setId(null);
        if (loan.getStatus() == null || loan.getStatus().isBlank()) {
            loan.setStatus("AVAILABLE");
        }
        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    public double calculateInterest(Loan loan) {
        return (loan.getAmount() * loan.getInterestRate() * loan.getDuration()) / 100.0;
    }
}
