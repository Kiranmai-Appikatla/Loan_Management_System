package com.safehaven.dto;

import com.safehaven.entity.SupportStatus;

import jakarta.validation.constraints.NotNull;

public record SupportRequestUpdateRequest(
        @NotNull SupportStatus status,
        String progressSummary
) {
}
