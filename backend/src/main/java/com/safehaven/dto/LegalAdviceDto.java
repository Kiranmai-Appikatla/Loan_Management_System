package com.safehaven.dto;

import java.time.LocalDateTime;

import com.safehaven.entity.LegalAdvice;

public record LegalAdviceDto(Long id, String legalAdvice, String advisorName, LocalDateTime timestamp) {

    public static LegalAdviceDto from(LegalAdvice advice) {
        return new LegalAdviceDto(advice.getId(), advice.getLegalAdvice(), advice.getAdvisorName(), advice.getTimestamp());
    }
}
