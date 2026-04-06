package com.safehaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safehaven.entity.LegalAdvice;

public interface LegalAdviceRepository extends JpaRepository<LegalAdvice, Long> {
}
