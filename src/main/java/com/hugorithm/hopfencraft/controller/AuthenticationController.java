package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.LoginResponseDTO;
import com.hugorithm.hopfencraft.dto.RegistrationDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody RegistrationDTO body) {
        return authenticationService.login(body.getUsername(), body.getPassword());
    }
}
