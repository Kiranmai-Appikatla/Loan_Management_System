package com.safehaven.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String concernType,
        @NotBlank String message
) {
}
