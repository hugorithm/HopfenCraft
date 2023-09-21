package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.user.OAuth2ApplicationUserDTO;
import com.hugorithm.hopfencraft.service.OAuth2Service;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;

    @GetMapping("/current-user")
    @RolesAllowed("USER")
    public ResponseEntity<OAuth2ApplicationUserDTO> getOAuth2User(@AuthenticationPrincipal Jwt jwt) {
        return oAuth2Service.getOAuth2User(jwt);
    }
}
