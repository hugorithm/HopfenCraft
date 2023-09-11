package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.exception.auth.InvalidTokenException;
import com.hugorithm.hopfencraft.exception.auth.PasswordMismatchException;
import com.hugorithm.hopfencraft.exception.auth.SamePasswordException;
import com.hugorithm.hopfencraft.exception.auth.WrongCredentialsException;
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
    private TokenService tokenService;

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
        when(tokenService.generatePasswordResetToken())
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        when(tokenService.URLDecodeToken(any()))
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

    @Test
    public void showPasswordResetForm_ValidToken_ReturnsOk() throws InvalidTokenException {
        // Arrange
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusDays(1));

        when(jwtService.getUserFromJwt(any())).thenReturn(user);
        when(tokenService.URLDecodeToken(any()))
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        when(tokenService.URLEncodeToken("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200"))
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4%7C2023-09-11T19%3A23%3A04%2E862591200");

        // Act
        ResponseEntity<?> response = userService.showPasswordResetForm(
                mock(Jwt.class),
                "2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4%7C2023-09-11T19%3A23%3A04%2E862591200"
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void showPasswordResetForm_InvalidToken_ReturnsForbidden() throws InvalidTokenException {
        // Arrange
        when(jwtService.getUserFromJwt(any())).thenThrow(new InvalidTokenException("Invalid token"));

        // Act
        ResponseEntity<?> response = userService.showPasswordResetForm(mock(Jwt.class), "invalidToken");

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void resetPassword_ValidRequest_ReturnsOk() throws InvalidTokenException, PasswordMismatchException, UsernameNotFoundException, SamePasswordException, WrongCredentialsException {
        // Arrange
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("oldPassword");
        user.setPasswordResetToken("bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusDays(1));

        when(jwtService.getUserFromJwt(any())).thenReturn(user);
        when(passwordEncoder.matches("oldPassword", "oldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(tokenService.URLDecodeToken(any()))
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        // Act
        ResponseEntity<?> response = userService.resetPassword(
                mock(Jwt.class),
                "2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4%7C2023-09-11T19%3A23%3A04%2E862591200",
                "oldPassword",
                "newPassword",
                "newPassword"
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void resetPassword_InvalidRequest_ReturnsBadRequest() throws InvalidTokenException, PasswordMismatchException, UsernameNotFoundException, SamePasswordException, WrongCredentialsException {
        // Arrange
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("oldPassword");
        user.setPasswordResetToken("bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusDays(1));

        when(jwtService.getUserFromJwt(any())).thenReturn(user);
        when(passwordEncoder.matches("oldPassword", "oldPassword")).thenReturn(false);
        when(tokenService.URLDecodeToken(any()))
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        // Act
        ResponseEntity<?> response = userService.resetPassword(
                mock(Jwt.class),
                "bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200",
                "oldPassword",
                "newPassword",
                "newPassword"
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
