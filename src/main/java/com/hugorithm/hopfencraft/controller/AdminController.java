package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.user.ApplicationUserDTO;
import com.hugorithm.hopfencraft.service.AdminService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<ApplicationUserDTO>> getUsers() {
        return adminService.getUsers();
    }

    @GetMapping("/user/{userId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ApplicationUserDTO> getUserById(@PathVariable Long userId) {
        return adminService.getUserById(userId);
    }

    @GetMapping("/user/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationUserDTO> getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }
}
