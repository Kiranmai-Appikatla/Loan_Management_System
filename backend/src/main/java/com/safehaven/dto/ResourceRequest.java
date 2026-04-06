package com.safehaven.dto;

import jakarta.validation.constraints.NotBlank;

public record ResourceRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String type,
        String contactLink
) {
}
