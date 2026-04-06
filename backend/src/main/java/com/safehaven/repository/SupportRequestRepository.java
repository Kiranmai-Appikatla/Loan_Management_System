package com.safehaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.safehaven.entity.SupportRequest;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {

    @Query("""
            select distinct sr from SupportRequest sr
            left join fetch sr.user
            left join fetch sr.counsellor
            left join fetch sr.legalAdvisor
            left join fetch sr.legalAdviceEntries
            left join fetch sr.counsellingNotes
            order by sr.updatedAt desc
            """)
    List<SupportRequest> findAllDetailed();

    @Query("""
            select distinct sr from SupportRequest sr
            left join fetch sr.user
            left join fetch sr.counsellor
            left join fetch sr.legalAdvisor
            left join fetch sr.legalAdviceEntries
            left join fetch sr.counsellingNotes
            where sr.user.id = :userId
            order by sr.updatedAt desc
            """)
    List<SupportRequest> findDetailedByUserId(@Param("userId") Long userId);

    @Query("""
            select distinct sr from SupportRequest sr
            left join fetch sr.user
            left join fetch sr.counsellor
            left join fetch sr.legalAdvisor
            left join fetch sr.legalAdviceEntries
            left join fetch sr.counsellingNotes
            where sr.counsellor.id = :providerId or sr.legalAdvisor.id = :providerId
            order by sr.updatedAt desc
            """)
    List<SupportRequest> findDetailedAssignedTo(@Param("providerId") Long providerId);
}
