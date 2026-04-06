package com.example.loanmanagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.Payment;
import com.example.loanmanagement.repository.LoanRepository;
import com.example.loanmanagement.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    public Payment createPayment(Payment payment) {
        Loan loan = loanRepository.findById(payment.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!"APPROVED".equalsIgnoreCase(loan.getStatus()) && !"COMPLETED".equalsIgnoreCase(loan.getStatus())) {
            throw new RuntimeException("Payments can only be made for approved loans");
        }

        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        payment.setId(null);
        Payment savedPayment = paymentRepository.save(payment);

        double totalPaid = paymentRepository.findByLoanId(payment.getLoanId()).stream()
                .mapToDouble(Payment::getAmountPaid)
                .sum();
        double totalInterest = (loan.getAmount() * loan.getInterestRate() * loan.getDuration()) / 100.0;
        double totalDue = loan.getAmount() + totalInterest;

        if (totalPaid >= totalDue) {
            loan.setStatus("COMPLETED");
            loanRepository.save(loan);
        }

        return savedPayment;
    }

    public List<Payment> getPaymentsByLoanId(Long loanId) {
        return paymentRepository.findByLoanId(loanId);
    }
}
