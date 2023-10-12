package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.user.PasswordResetRequestDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetResponseDTO;
import com.hugorithm.hopfencraft.exception.email.EmailSendingFailedException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
        assertEquals(200, response.getStatusCodeValue());
    }


}
