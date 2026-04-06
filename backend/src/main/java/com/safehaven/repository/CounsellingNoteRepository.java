package com.safehaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safehaven.entity.CounsellingNote;

public interface CounsellingNoteRepository extends JpaRepository<CounsellingNote, Long> {
}
