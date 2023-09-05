package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.LoginResponseDTO;
import com.hugorithm.hopfencraft.dto.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@RequestBody UserRegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserRegistrationDTO body) {
        return authenticationService.login(body.getUsername(), body.getPassword());
    }
}
