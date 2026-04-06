package com.safehaven.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.safehaven.dto.AuthRequest;
import com.safehaven.dto.AuthResponse;
import com.safehaven.dto.RegisterRequest;
import com.safehaven.dto.UserDto;
import com.safehaven.entity.Role;
import com.safehaven.entity.User;
import com.safehaven.repository.UserRepository;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(BAD_REQUEST, "An account with that email already exists");
        }

        User user = new User(
                request.name(),
                request.email().toLowerCase(),
                passwordEncoder.encode(request.password()),
                request.role()
        );
        userRepository.save(user);

        return buildAuthResponse(user, "Registration successful");
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
        );

        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        return buildAuthResponse(user, "Login successful");
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    }

    public List<UserDto> listUsers() {
        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

    public List<UserDto> listProviders() {
        return userRepository.findByRoleInOrderByNameAsc(List.of(Role.COUNSELLOR, Role.LEGAL_ADVISOR))
                .stream()
                .map(UserDto::from)
                .toList();
    }

    @Transactional
    public UserDto updateRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        user.setRole(role);
        return UserDto.from(userRepository.save(user));
    }

    private AuthResponse buildAuthResponse(User user, String message) {
        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails, java.util.Map.of("role", user.getRole().name()));
        return new AuthResponse(token, UserDto.from(user), message);
    }
}
