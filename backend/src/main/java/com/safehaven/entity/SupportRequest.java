package com.safehaven.entity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "support_requests")
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsellor_id")
    private User counsellor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_advisor_id")
    private User legalAdvisor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SupportStatus status;

    @Column(columnDefinition = "TEXT")
    private String progressSummary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "supportRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LegalAdvice> legalAdviceEntries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "supportRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CounsellingNote> counsellingNotes = new LinkedHashSet<>();

    public SupportRequest() {
    }

    public SupportRequest(User user, User counsellor, User legalAdvisor, String message, SupportStatus status, String progressSummary) {
        this.user = user;
        this.counsellor = counsellor;
        this.legalAdvisor = legalAdvisor;
        this.message = message;
        this.status = status;
        this.progressSummary = progressSummary;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCounsellor() {
        return counsellor;
    }

    public void setCounsellor(User counsellor) {
        this.counsellor = counsellor;
    }

    public User getLegalAdvisor() {
        return legalAdvisor;
    }

    public void setLegalAdvisor(User legalAdvisor) {
        this.legalAdvisor = legalAdvisor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SupportStatus getStatus() {
        return status;
    }

    public void setStatus(SupportStatus status) {
        this.status = status;
    }

    public String getProgressSummary() {
        return progressSummary;
    }

    public void setProgressSummary(String progressSummary) {
        this.progressSummary = progressSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<LegalAdvice> getLegalAdviceEntries() {
        return legalAdviceEntries;
    }

    public Set<CounsellingNote> getCounsellingNotes() {
        return counsellingNotes;
    }
}
