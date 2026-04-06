package com.safehaven.dto;

import jakarta.validation.constraints.NotBlank;

public record CounsellingNoteRequest(@NotBlank String notes) {
}
