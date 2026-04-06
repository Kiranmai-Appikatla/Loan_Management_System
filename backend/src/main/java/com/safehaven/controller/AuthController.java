package com.safehaven.controller;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safehaven.dto.AuthRequest;
import com.safehaven.dto.AuthResponse;
import com.safehaven.dto.RegisterRequest;
import com.safehaven.dto.UserDto;
import com.safehaven.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Validated @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Validated @RequestBody AuthRequest request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    public UserDto currentUser(Authentication authentication) {
        return UserDto.from(userService.getByEmail(authentication.getName()));
    }
}
