package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.authentication.LoginDTO;
import com.hugorithm.hopfencraft.dto.authentication.LoginResponseDTO;
import com.hugorithm.hopfencraft.dto.authentication.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.authentication.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.dto.user.ApplicationUserDTO;
import com.hugorithm.hopfencraft.service.AuthenticationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@RequestBody @Valid UserRegistrationDTO body) {
        return authenticationService.registerUser(body);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO body) {
        return authenticationService.login(body);
    }

    @GetMapping("/current-user")
    @RolesAllowed({"USER", "ADMIN"})
    public ResponseEntity<ApplicationUserDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return authenticationService.getCurrentUser(jwt);
    }
}
