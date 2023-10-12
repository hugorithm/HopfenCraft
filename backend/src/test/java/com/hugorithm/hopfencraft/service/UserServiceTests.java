package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetRequestDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetResponseDTO;
import com.hugorithm.hopfencraft.exception.auth.InvalidTokenException;
import com.hugorithm.hopfencraft.exception.email.EmailSendingFailedException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    private JwtService jwtService;
    private EmailService emailService;
    private UserRepository userRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        jwtService = Mockito.mock(JwtService.class);
        emailService = Mockito.mock(EmailService.class);
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(jwtService, emailService, userRepository, passwordEncoder);
    }
    @Test
    public void testSendPasswordResetRequest() throws EmailSendingFailedException, UsernameNotFoundException {
        // Arrange
        JwtService jwtService = Mockito.mock(JwtService.class);
        EmailService emailService = Mockito.mock(EmailService.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        UserService userService = new UserService(jwtService, emailService, userRepository, passwordEncoder);

        PasswordResetRequestDTO resetRequestDTO = new PasswordResetRequestDTO("username");
        ApplicationUser user = new ApplicationUser();
        user.setUsername("username");
        user.setEmail("user@example.com");


        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(jwtService
                .generatePasswordResetToken())
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");
        when(jwtService
                .URLDecodeToken("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200"))
                .thenReturn("2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200");

        // Act
        ResponseEntity<PasswordResetResponseDTO> response = userService.sendPasswordResetRequest(resetRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testShowPasswordResetForm() {
        // Mock data and dependencies
        String token = "2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200";
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusMinutes(15));
        when(jwtService.URLDecodeToken(token)).thenReturn(token);
        when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(user));

        // Test showing the password reset form with a valid token
        ResponseEntity<?> response = userService.showPasswordResetForm(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testShowPasswordResetFormInvalidToken() {
        // Mock data and dependencies
        String token = "invalidToken";
        when(jwtService.URLDecodeToken(token)).thenThrow(new InvalidTokenException("Invalid token"));

        // Test showing the password reset form with an invalid token
        ResponseEntity<?> response = userService.showPasswordResetForm(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testResetPassword() {
        // Mock data and dependencies
        String token = "2bcb63ba728ea29e9763612ae885659ec0e2c6dc9d0d54ee7b8fb1e620aeb3e4|2023-09-11T19:23:04.862591200";
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusMinutes(15));
        when(jwtService.URLDecodeToken(token)).thenReturn(token);
        when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(ApplicationUser.class))).thenReturn(user);

        PasswordResetDTO resetDTO = new PasswordResetDTO("newPassword123!", "newPassword123!");

        // Test resetting the user's password
        ResponseEntity<?> response = userService.resetPassword(token, resetDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testResetPasswordWithMismatchedPasswords() {
        // Mock data and dependencies
        String token = "sampleToken";
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiration(LocalDateTime.now().plusMinutes(15));
        when(jwtService.URLDecodeToken(token)).thenReturn(token);
        when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(ApplicationUser.class))).thenReturn(user);

        PasswordResetDTO resetDTO = new PasswordResetDTO("newPassword", "differentPassword");

        // Test resetting the user's password with mismatched passwords
        ResponseEntity<?> response = userService.resetPassword(token, resetDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testResetPasswordWithExpiredToken() {
        // Mock data and dependencies
        String token = "sampleToken";
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiration(LocalDateTime.now().minusMinutes(15));
        when(jwtService.URLDecodeToken(token)).thenReturn(token);
        when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(user));

        PasswordResetDTO resetDTO = new PasswordResetDTO("newPassword", "newPassword");

        // Test resetting the user's password with an expired token
        ResponseEntity<?> response = userService.resetPassword(token, resetDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
