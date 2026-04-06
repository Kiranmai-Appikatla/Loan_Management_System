package com.safehaven.dto;

import java.time.LocalDateTime;

import com.safehaven.entity.CounsellingNote;

public record CounsellingNoteDto(Long id, String notes, String counsellorName, LocalDateTime timestamp) {

    public static CounsellingNoteDto from(CounsellingNote note) {
        return new CounsellingNoteDto(note.getId(), note.getNotes(), note.getCounsellorName(), note.getTimestamp());
    }
}
