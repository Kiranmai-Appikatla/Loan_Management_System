package com.safehaven.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "legal_advice")
public class LegalAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "support_request_id", nullable = false)
    private SupportRequest supportRequest;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String legalAdvice;

    @Column(nullable = false, length = 120)
    private String advisorName;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LegalAdvice() {
    }

    public LegalAdvice(SupportRequest supportRequest, String legalAdvice, String advisorName) {
        this.supportRequest = supportRequest;
        this.legalAdvice = legalAdvice;
        this.advisorName = advisorName;
    }

    @PrePersist
    void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SupportRequest getSupportRequest() {
        return supportRequest;
    }

    public void setSupportRequest(SupportRequest supportRequest) {
        this.supportRequest = supportRequest;
    }

    public String getLegalAdvice() {
        return legalAdvice;
    }

    public void setLegalAdvice(String legalAdvice) {
        this.legalAdvice = legalAdvice;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
