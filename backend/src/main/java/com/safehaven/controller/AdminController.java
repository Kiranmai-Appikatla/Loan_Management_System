package com.safehaven.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safehaven.dto.AdminOverviewDto;
import com.safehaven.dto.UserDto;
import com.safehaven.entity.Role;
import com.safehaven.service.SupportRequestService;
import com.safehaven.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final SupportRequestService supportRequestService;

    public AdminController(UserService userService, SupportRequestService supportRequestService) {
        this.userService = userService;
        this.supportRequestService = supportRequestService;
    }

    @GetMapping("/users")
    public List<UserDto> users() {
        return userService.listUsers();
    }

    @PatchMapping("/users/{id}/role")
    public UserDto updateRole(@PathVariable Long id, @RequestBody Role role) {
        return userService.updateRole(id, role);
    }

    @GetMapping("/overview")
    public AdminOverviewDto overview() {
        return supportRequestService.getAdminOverview();
    }
}
