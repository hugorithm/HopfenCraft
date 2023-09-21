package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/reset-password-request")
    @RolesAllowed("USER")
    public ResponseEntity<String> sendPasswordResetRequest(@AuthenticationPrincipal Jwt jwt) {
        return userService.sendPasswordResetRequest(jwt);
    }

    @GetMapping("/reset-password")
    @RolesAllowed("USER")
    public ResponseEntity<?> resetPassword(@AuthenticationPrincipal Jwt jwt, @RequestParam String token) {
        return userService.showPasswordResetForm(jwt, token);
    }

    @PostMapping("/reset-password")
    @RolesAllowed("USER")
    public ResponseEntity<?> resetPassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PasswordResetDTO body, @RequestParam String token) {
        return userService.resetPassword(jwt, token, body);
    }
}
