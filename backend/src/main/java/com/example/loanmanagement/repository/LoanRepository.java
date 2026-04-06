package com.example.loanmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanmanagement.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByLenderId(Long lenderId);
}
