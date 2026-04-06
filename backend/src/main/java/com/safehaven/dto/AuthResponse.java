package com.safehaven.dto;

public record AuthResponse(String token, UserDto user, String message) {
}
