package com.safehaven.dto;

import com.safehaven.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long") String password,
        @NotNull Role role
) {
}
