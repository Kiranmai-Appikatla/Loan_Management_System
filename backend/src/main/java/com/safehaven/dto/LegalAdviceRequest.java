package com.safehaven.dto;

import jakarta.validation.constraints.NotBlank;

public record LegalAdviceRequest(@NotBlank String legalAdvice) {
}
