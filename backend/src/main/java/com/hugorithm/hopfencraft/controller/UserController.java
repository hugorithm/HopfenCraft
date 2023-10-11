package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetRequestDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetResponseDTO;
import com.hugorithm.hopfencraft.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/reset-password-request")
    public ResponseEntity<PasswordResetResponseDTO> sendPasswordResetRequest(@Valid @RequestBody PasswordResetRequestDTO dto) {
        return userService.sendPasswordResetRequest(dto);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token) {
        return userService.showPasswordResetForm(token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDTO body, @RequestParam String token) {
        return userService.resetPassword(token, body);
    }
}
