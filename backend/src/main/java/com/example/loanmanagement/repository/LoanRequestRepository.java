package com.example.loanmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanmanagement.entity.LoanRequest;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    Optional<LoanRequest> findByLoanIdAndBorrowerId(Long loanId, Long borrowerId);
    List<LoanRequest> findByLoanIdIn(List<Long> loanIds);
    List<LoanRequest> findByBorrowerId(Long borrowerId);
}
