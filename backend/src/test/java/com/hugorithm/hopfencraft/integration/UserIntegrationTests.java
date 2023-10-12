package com.hugorithm.hopfencraft.integration;

import com.hugorithm.hopfencraft.config.TestConfig;
import com.hugorithm.hopfencraft.dto.authentication.LoginDTO;
import com.hugorithm.hopfencraft.dto.authentication.LoginResponseDTO;
import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetRequestDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetResponseDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import com.hugorithm.hopfencraft.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {TestConfig.class})
public class UserIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    public void sendPasswordResetRequest_ValidUser_ReturnsOk() {
        // Perform login or JWT retrieval here if needed
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        PasswordResetRequestDTO dto = new PasswordResetRequestDTO("testuser");
        ResponseEntity<PasswordResetResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/user/reset-password-request",
                dto,
                PasswordResetResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PasswordResetResponseDTO("Password reset email sent successfully"), response.getBody());

        // Add more assertions as needed
    }


    @Test
    public void resetPassword_ValidRequest_ReturnsOk() {
        // Perform login or JWT retrieval here if needed
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO("testuser");

        //Send token
        ResponseEntity<PasswordResetResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/user/reset-password-request",
                dto,
                PasswordResetResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PasswordResetResponseDTO("Password reset email sent successfully"), response.getBody());

        ApplicationUser user = userRepository.findByUsername("testuser").get();

        String token = user.getPasswordResetToken();
        // Assume you have a valid reset token and a PasswordResetDTO with new passwords

        ResponseEntity<?> response2 = restTemplate.getForEntity(
                "http://localhost:" + port + "/user/reset-password?token=" + token,
                ResponseEntity.class
        );

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNull(response2.getBody());
        // Add more assertions as needed

        PasswordResetDTO passwordResetDTO = new PasswordResetDTO(
                "NewPassword123!",
                "NewPassword123!"
        );


        HttpEntity<PasswordResetDTO> requestEntity = new HttpEntity<>(passwordResetDTO);

        ResponseEntity<Void> response3 = restTemplate.exchange(
                "http://localhost:" + port + "/user/reset-password?token=" + token,
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNull(response3.getBody());
    }

    @Test
    public void resetPassword_InvalidRequest_ReturnsBadRequest() {
        // Perform login or JWT retrieval here if needed
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO("testuser");

        ResponseEntity<PasswordResetResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/user/reset-password-request",
                dto,
                PasswordResetResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PasswordResetResponseDTO("Password reset email sent successfully"), response.getBody());

        // Assume you have an invalid PasswordResetDTO
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO(
                "wrongPassword123!",
                "NewPassword123!"
        );

        ApplicationUser user = userRepository.findByUsername("testuser").get();

        String token = jwtService.URLEncodeToken(user.getPasswordResetToken());


        ResponseEntity<?> response2 = restTemplate.getForEntity(
                "http://localhost:" + port + "/user/reset-password?token=" + token,
                ResponseEntity.class
        );


        assertEquals(HttpStatus.OK, response2.getStatusCode());


        HttpEntity<PasswordResetDTO> requestEntity = new HttpEntity<>(passwordResetDTO);
        ResponseEntity<Void> response3 = restTemplate.exchange(
                "http://localhost:" + port + "/user/reset-password?token=" + token,
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
        // Add more assertions as needed
    }


}
