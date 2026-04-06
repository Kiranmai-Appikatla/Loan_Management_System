package com.safehaven.controller;

import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safehaven.dto.ContactRequest;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @PostMapping
    public Map<String, String> submit(@Validated @RequestBody ContactRequest request) {
        return Map.of(
                "message", "Your request has been received. A SafeHaven team member will follow up soon.",
                "submittedBy", request.name(),
                "concernType", request.concernType()
        );
    }
}
