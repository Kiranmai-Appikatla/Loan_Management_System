package com.safehaven.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safehaven.dto.CounsellingNoteRequest;
import com.safehaven.dto.LegalAdviceRequest;
import com.safehaven.dto.SupportRequestCreateRequest;
import com.safehaven.dto.SupportRequestDto;
import com.safehaven.dto.SupportRequestUpdateRequest;
import com.safehaven.entity.User;
import com.safehaven.service.SupportRequestService;
import com.safehaven.service.UserService;

@RestController
@RequestMapping("/api/support-requests")
public class SupportRequestController {

    private final SupportRequestService supportRequestService;
    private final UserService userService;

    public SupportRequestController(SupportRequestService supportRequestService, UserService userService) {
        this.supportRequestService = supportRequestService;
        this.userService = userService;
    }

    @GetMapping
    public List<SupportRequestDto> listForCurrentUser(Principal principal) {
        return supportRequestService.listFor(currentUser(principal));
    }

    @PostMapping
    public SupportRequestDto create(Principal principal, @Validated @RequestBody SupportRequestCreateRequest request) {
        return supportRequestService.create(currentUser(principal), request);
    }

    @PatchMapping("/{id}/status")
    public SupportRequestDto updateStatus(
            @PathVariable Long id,
            Principal principal,
            @Validated @RequestBody SupportRequestUpdateRequest request
    ) {
        return supportRequestService.updateStatus(id, currentUser(principal), request);
    }

    @PostMapping("/{id}/legal-advice")
    public SupportRequestDto addLegalAdvice(
            @PathVariable Long id,
            Principal principal,
            @Validated @RequestBody LegalAdviceRequest request
    ) {
        return supportRequestService.addLegalAdvice(id, currentUser(principal), request);
    }

    @PostMapping("/{id}/counselling-notes")
    public SupportRequestDto addCounsellingNotes(
            @PathVariable Long id,
            Principal principal,
            @Validated @RequestBody CounsellingNoteRequest request
    ) {
        return supportRequestService.addCounsellingNote(id, currentUser(principal), request);
    }

    private User currentUser(Principal principal) {
        return userService.getByEmail(principal.getName());
    }
}
