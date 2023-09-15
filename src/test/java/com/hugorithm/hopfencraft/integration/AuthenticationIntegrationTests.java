package com.hugorithm.hopfencraft.integration;

import com.hugorithm.hopfencraft.dto.authentication.LoginDTO;
import com.hugorithm.hopfencraft.dto.authentication.LoginResponseDTO;
import com.hugorithm.hopfencraft.dto.authentication.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.authentication.UserRegistrationResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    public void RegisterUser_ValidInput_ReturnsOk() {
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "ValidPass123!", "validemaildiff@example.com");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals("validusername", responseBody.getUsername());
        assertEquals("validemaildiff@example.com", responseBody.getEmail());

    }

    @Test
    public void RegisterUser_InvalidEmail_ReturnsBadRequest() {
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "ValidPass123!", "invalidemail");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNull(responseBody);
    }

    @Test
    public void RegisterUser_InvalidSameEmail_ReturnsBadRequest() {
        UserRegistrationDTO validInput = new UserRegistrationDTO("test", "ValidPass123!", "test@example.com");
        UserRegistrationDTO validInput2 = new UserRegistrationDTO("test2", "ValidPass123!", "test@example.com");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("test", responseBody.getUsername());
        assertEquals("test@example.com", responseBody.getEmail());

        ResponseEntity<UserRegistrationResponseDTO> response2 = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput2,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

        UserRegistrationResponseDTO responseBody2 = response2.getBody();
        assertNull(responseBody2);
    }

    @Test
    public void RegisterUser_InvalidSameUsername_ReturnsBadRequest() {
        UserRegistrationDTO validInput = new UserRegistrationDTO("test", "ValidPass123!", "test@example.com");
        UserRegistrationDTO validInput2 = new UserRegistrationDTO("test", "ValidPass123!", "test2@example.com");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("test", responseBody.getUsername());
        assertEquals("test@example.com", responseBody.getEmail());

        ResponseEntity<UserRegistrationResponseDTO> response2 = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput2,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

        UserRegistrationResponseDTO responseBody2 = response2.getBody();
        assertNull(responseBody2);
    }

    @Test
    public void RegisterUser_InvalidUsername_ReturnsBadRequest() {
        UserRegistrationDTO validInput = new UserRegistrationDTO("in", "ValidPass123!", "validemail@example.com");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNull(responseBody);
    }

    @Test
    public void RegisterUser_InvalidPassword_ReturnsBadRequest() {
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "invalidpassword", "validemail@example.com");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        UserRegistrationResponseDTO responseBody = response.getBody();
        assertNull(responseBody);
    }

    @Test
    public void Login_BadCredentials_ReturnsBadRequest() {
        LoginDTO invalidInput = new LoginDTO("validusername", "invalidpassword");

        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                invalidInput,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        LoginResponseDTO responseBody = response.getBody();
        assertNull(responseBody);
    }

    @Test
    public void Login_ValidCredentials_ReturnsOk() {
        //Register User
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "ValidPass123!", "validemaildiff@example.com");

        ResponseEntity<UserRegistrationResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/register",
                validInput,
                UserRegistrationResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //Login
        LoginDTO validInput2 = new LoginDTO("validusername", "ValidPass123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                validInput2,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        //Assert
        LoginResponseDTO responseBody = loginResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(responseBody.getUsername(), validInput.getUsername());
        assertEquals(responseBody.getEmail(), validInput.getEmail());
    }



}
