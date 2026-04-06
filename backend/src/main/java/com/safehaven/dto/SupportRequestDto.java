package com.safehaven.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.safehaven.entity.SupportRequest;
import com.safehaven.entity.SupportStatus;

public record SupportRequestDto(
        Long id,
        UserDto user,
        UserDto counsellor,
        UserDto legalAdvisor,
        String message,
        SupportStatus status,
        String progressSummary,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<LegalAdviceDto> legalAdvice,
        List<CounsellingNoteDto> counsellingNotes
) {

    public static SupportRequestDto from(SupportRequest supportRequest) {
        return new SupportRequestDto(
                supportRequest.getId(),
                UserDto.from(supportRequest.getUser()),
                supportRequest.getCounsellor() == null ? null : UserDto.from(supportRequest.getCounsellor()),
                supportRequest.getLegalAdvisor() == null ? null : UserDto.from(supportRequest.getLegalAdvisor()),
                supportRequest.getMessage(),
                supportRequest.getStatus(),
                supportRequest.getProgressSummary(),
                supportRequest.getCreatedAt(),
                supportRequest.getUpdatedAt(),
                supportRequest.getLegalAdviceEntries().stream()
                        .map(LegalAdviceDto::from)
                        .sorted(Comparator.comparing(LegalAdviceDto::timestamp).reversed())
                        .toList(),
                supportRequest.getCounsellingNotes().stream()
                        .map(CounsellingNoteDto::from)
                        .sorted(Comparator.comparing(CounsellingNoteDto::timestamp).reversed())
                        .toList()
        );
    }
}
