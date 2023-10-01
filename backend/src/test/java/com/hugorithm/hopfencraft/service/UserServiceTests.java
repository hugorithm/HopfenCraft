package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.exception.auth.InvalidTokenException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendPasswordResetRequest_ValidJwt_ReturnsOk() throws InvalidTokenException {
        // Arrange
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusDays(1));

        when(jwtService.getUserFromJwt(any())).thenReturn(user);
        when(jwtService.generatePasswordResetToken())
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        when(jwtService.URLDecodeToken(any()))
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        when(userRepository.save(any())).thenReturn(user);

        // Act
        ResponseEntity<String> response = userService.sendPasswordResetRequest(mock(Jwt.class));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void sendPasswordResetRequest_InvalidJwt_ReturnsBadRequest() throws InvalidTokenException {
        // Arrange
        when(jwtService.getUserFromJwt(any())).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<String> response = userService.sendPasswordResetRequest(mock(Jwt.class));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
