package com.example.loanmanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanmanagement.entity.LoanRequest;
import com.example.loanmanagement.service.LoanRequestService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    @Autowired
    private LoanRequestService loanRequestService;

    @PostMapping
    public ResponseEntity<?> createLoanRequest(@RequestBody LoanRequest loanRequest) {
        try {
            return ResponseEntity.ok(loanRequestService.createLoanRequest(loanRequest));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(exception.getMessage()));
        }
    }

    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<?> getRequestsByLenderId(@PathVariable Long lenderId) {
        return ResponseEntity.ok(loanRequestService.getRequestsByLenderId(lenderId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(loanRequestService.approveRequest(id));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(exception.getMessage()));
        }
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
