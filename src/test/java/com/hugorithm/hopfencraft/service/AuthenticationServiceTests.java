package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.config.DataInitialization;
import com.hugorithm.hopfencraft.dto.LoginDTO;
import com.hugorithm.hopfencraft.dto.LoginResponseDTO;
import com.hugorithm.hopfencraft.dto.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationServiceTests {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private DataInitialization dataInitialization;


    @Test
    public void testRegisterUser_ValidInput_ReturnsOk() {
        dataInitialization.run();
        // Implement test logic for a successful user registration with valid input.
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "ValidPass123!", "validemail@example.com");
        Role userRole = new Role("USER");

        // Mock dependencies
        when(userRepository.findByUsername("validusername")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("validemail@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("ValidPass123!")).thenReturn("encodedPassword");


        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(userRole));
        // Call the service method
        ResponseEntity<UserRegistrationResponseDTO> response = authenticationService.registerUser(validInput.getUsername(), validInput.getPassword(), validInput.getEmail());

        // Assert the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("validusername", responseBody.getUsername());
        assertEquals("validemail@example.com", responseBody.getEmail());
    }

    @Test
    public void testLogin_BadCredentials_ReturnsBadRequest() {
        dataInitialization.run();
        // Implement test logic for user login with incorrect credentials.
        LoginDTO invalidInput = new LoginDTO("validusername", "invalidpassword");

        // Mock dependencies
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Authentication failed") {
        });

        // Call the service method
        ResponseEntity<LoginResponseDTO> response = authenticationService.login(invalidInput.getUsername(), invalidInput.getPassword());

        // Assert the expected behavior
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    public void testLogin_Successful_ReturnsOk() {
        dataInitialization.run();
        // Implement test logic for a successful user login.
        LoginDTO validInput = new LoginDTO("validusername", "ValidPass123!");
        ApplicationUser user = new ApplicationUser();
        user.setUsername("validusername");
        user.setEmail("validemail@example.com");
        Authentication auth = mock(Authentication.class);

        // Mock dependencies
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenService.generateJwt(auth)).thenReturn("jwtToken");
        when(userRepository.findByUsername("validusername")).thenReturn(Optional.of(user));

        // Call the service method
        ResponseEntity<LoginResponseDTO> response = authenticationService.login(validInput.getUsername(), validInput.getPassword());

        // Assert the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("validusername", response.getBody().getUsername());
        assertEquals("validemail@example.com", response.getBody().getEmail());
    }
}






