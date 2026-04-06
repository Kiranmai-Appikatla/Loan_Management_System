package com.safehaven.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SupportRequestCreateRequest(
        @NotNull Long counsellorId,
        @NotNull Long legalAdvisorId,
        @NotBlank String message
) {
}
