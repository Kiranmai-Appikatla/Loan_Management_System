package com.example.loanmanagement.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.LoanRequest;
import com.example.loanmanagement.repository.LoanRepository;
import com.example.loanmanagement.repository.LoanRequestRepository;

@Service
public class LoanRequestService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private LoanRepository loanRepository;

    public LoanRequest createLoanRequest(LoanRequest loanRequest) {
        Loan loan = loanRepository.findById(loanRequest.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loanRequestRepository.findByLoanIdAndBorrowerId(loanRequest.getLoanId(), loanRequest.getBorrowerId())
                .ifPresent(existing -> {
                    throw new RuntimeException("Duplicate loan request is not allowed");
                });

        if (!"AVAILABLE".equalsIgnoreCase(loan.getStatus())) {
            throw new RuntimeException("Loan is not available for new requests");
        }

        loanRequest.setId(null);
        loanRequest.setStatus("PENDING");
        loan.setStatus("PENDING");
        loanRepository.save(loan);
        return loanRequestRepository.save(loanRequest);
    }

    public List<LoanRequest> getRequestsByLenderId(Long lenderId) {
        List<Long> loanIds = loanRepository.findByLenderId(lenderId).stream()
                .map(Loan::getId)
                .collect(Collectors.toList());

        if (loanIds.isEmpty()) {
            return Collections.emptyList();
        }

        return loanRequestRepository.findByLoanIdIn(loanIds);
    }

    public LoanRequest approveRequest(Long requestId) {
        LoanRequest loanRequest = loanRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        Loan loan = loanRepository.findById(loanRequest.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loanRequest.setStatus("APPROVED");
        loan.setStatus("APPROVED");
        loanRepository.save(loan);
        return loanRequestRepository.save(loanRequest);
    }
}
