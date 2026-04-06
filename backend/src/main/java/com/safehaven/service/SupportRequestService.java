package com.safehaven.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.safehaven.dto.AdminOverviewDto;
import com.safehaven.dto.CounsellingNoteRequest;
import com.safehaven.dto.LegalAdviceRequest;
import com.safehaven.dto.SupportRequestCreateRequest;
import com.safehaven.dto.SupportRequestDto;
import com.safehaven.dto.SupportRequestUpdateRequest;
import com.safehaven.entity.CounsellingNote;
import com.safehaven.entity.LegalAdvice;
import com.safehaven.entity.Role;
import com.safehaven.entity.SupportRequest;
import com.safehaven.entity.SupportStatus;
import com.safehaven.entity.User;
import com.safehaven.repository.CounsellingNoteRepository;
import com.safehaven.repository.LegalAdviceRepository;
import com.safehaven.repository.ResourceItemRepository;
import com.safehaven.repository.SupportRequestRepository;
import com.safehaven.repository.UserRepository;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SupportRequestService {

    private final SupportRequestRepository supportRequestRepository;
    private final UserRepository userRepository;
    private final LegalAdviceRepository legalAdviceRepository;
    private final CounsellingNoteRepository counsellingNoteRepository;
    private final ResourceItemRepository resourceItemRepository;

    public SupportRequestService(
            SupportRequestRepository supportRequestRepository,
            UserRepository userRepository,
            LegalAdviceRepository legalAdviceRepository,
            CounsellingNoteRepository counsellingNoteRepository,
            ResourceItemRepository resourceItemRepository
    ) {
        this.supportRequestRepository = supportRequestRepository;
        this.userRepository = userRepository;
        this.legalAdviceRepository = legalAdviceRepository;
        this.counsellingNoteRepository = counsellingNoteRepository;
        this.resourceItemRepository = resourceItemRepository;
    }

    @Transactional
    public SupportRequestDto create(User requester, SupportRequestCreateRequest request) {
        if (requester.getRole() != Role.VICTIM_SURVIVOR) {
            throw new ResponseStatusException(FORBIDDEN, "Only survivor accounts can create support requests");
        }

        User counsellor = userRepository.findById(request.counsellorId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Counsellor not found"));
        User legalAdvisor = userRepository.findById(request.legalAdvisorId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Legal advisor not found"));

        if (counsellor.getRole() != Role.COUNSELLOR) {
            throw new ResponseStatusException(BAD_REQUEST, "Selected counsellor is not a counsellor account");
        }
        if (legalAdvisor.getRole() != Role.LEGAL_ADVISOR) {
            throw new ResponseStatusException(BAD_REQUEST, "Selected legal advisor is not a legal advisor account");
        }

        SupportRequest saved = supportRequestRepository.save(
                new SupportRequest(requester, counsellor, legalAdvisor, request.message(), SupportStatus.OPEN, "New request created")
        );
        return SupportRequestDto.from(reload(saved.getId()));
    }

    public List<SupportRequestDto> listFor(User requester) {
        List<SupportRequest> results;
        if (requester.getRole() == Role.ADMIN) {
            results = supportRequestRepository.findAllDetailed();
        } else if (requester.getRole() == Role.VICTIM_SURVIVOR) {
            results = supportRequestRepository.findDetailedByUserId(requester.getId());
        } else {
            results = supportRequestRepository.findDetailedAssignedTo(requester.getId());
        }
        return results.stream().map(SupportRequestDto::from).toList();
    }

    @Transactional
    public SupportRequestDto updateStatus(Long id, User requester, SupportRequestUpdateRequest request) {
        SupportRequest supportRequest = supportRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Support request not found"));
        assertCanAccess(supportRequest, requester);
        supportRequest.setStatus(request.status());
        if (request.progressSummary() != null && !request.progressSummary().isBlank()) {
            supportRequest.setProgressSummary(request.progressSummary());
        }
        return SupportRequestDto.from(reload(supportRequestRepository.save(supportRequest).getId()));
    }

    @Transactional
    public SupportRequestDto addLegalAdvice(Long id, User requester, LegalAdviceRequest request) {
        if (requester.getRole() != Role.LEGAL_ADVISOR && requester.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(FORBIDDEN, "Only legal advisors or admins can add legal advice");
        }
        SupportRequest supportRequest = supportRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Support request not found"));
        assertCanAccess(supportRequest, requester);
        legalAdviceRepository.save(new LegalAdvice(supportRequest, request.legalAdvice(), requester.getName()));
        supportRequest.setStatus(SupportStatus.IN_PROGRESS);
        supportRequest.setProgressSummary("Legal guidance updated");
        supportRequestRepository.save(supportRequest);
        return SupportRequestDto.from(reload(id));
    }

    @Transactional
    public SupportRequestDto addCounsellingNote(Long id, User requester, CounsellingNoteRequest request) {
        if (requester.getRole() != Role.COUNSELLOR && requester.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(FORBIDDEN, "Only counsellors or admins can add counselling notes");
        }
        SupportRequest supportRequest = supportRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Support request not found"));
        assertCanAccess(supportRequest, requester);
        counsellingNoteRepository.save(new CounsellingNote(supportRequest, request.notes(), requester.getName()));
        supportRequest.setStatus(SupportStatus.IN_PROGRESS);
        supportRequest.setProgressSummary("Counselling notes updated");
        supportRequestRepository.save(supportRequest);
        return SupportRequestDto.from(reload(id));
    }

    public AdminOverviewDto getAdminOverview() {
        Map<String, Long> roleCounts = new HashMap<>();
        for (Role role : Role.values()) {
            roleCounts.put(role.name(), userRepository.findAll().stream().filter(user -> user.getRole() == role).count());
        }

        Map<String, Long> statusCounts = new HashMap<>();
        for (SupportStatus status : SupportStatus.values()) {
            statusCounts.put(status.name(), supportRequestRepository.findAllDetailed().stream().filter(item -> item.getStatus() == status).count());
        }

        return new AdminOverviewDto(
                userRepository.count(),
                resourceItemRepository.count(),
                supportRequestRepository.count(),
                roleCounts,
                statusCounts,
                supportRequestRepository.findAllDetailed().stream().limit(5).map(SupportRequestDto::from).toList()
        );
    }

    private SupportRequest reload(Long id) {
        return supportRequestRepository.findAllDetailed().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Support request not found"));
    }

    private void assertCanAccess(SupportRequest supportRequest, User requester) {
        boolean allowed = requester.getRole() == Role.ADMIN
                || supportRequest.getUser().getId().equals(requester.getId())
                || (supportRequest.getCounsellor() != null && supportRequest.getCounsellor().getId().equals(requester.getId()))
                || (supportRequest.getLegalAdvisor() != null && supportRequest.getLegalAdvisor().getId().equals(requester.getId()));
        if (!allowed) {
            throw new ResponseStatusException(FORBIDDEN, "You do not have access to this support request");
        }
    }
}
