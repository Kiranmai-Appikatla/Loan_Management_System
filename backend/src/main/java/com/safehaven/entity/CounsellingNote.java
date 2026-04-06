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
@Table(name = "counselling_notes")
public class CounsellingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "support_request_id", nullable = false)
    private SupportRequest supportRequest;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, length = 120)
    private String counsellorName;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public CounsellingNote() {
    }

    public CounsellingNote(SupportRequest supportRequest, String notes, String counsellorName) {
        this.supportRequest = supportRequest;
        this.notes = notes;
        this.counsellorName = counsellorName;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCounsellorName() {
        return counsellorName;
    }

    public void setCounsellorName(String counsellorName) {
        this.counsellorName = counsellorName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
