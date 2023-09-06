package com.hugorithm.hopfencraft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugorithm.hopfencraft.dto.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(AuthenticationController.class)
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void testRegisterUser_ValidInput_ReturnsOk() throws Exception {
        // Define valid input data
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "ValidPass123!", "validemail@example.com");

        // Define the expected response from the service
        UserRegistrationResponseDTO expectedResponse = new UserRegistrationResponseDTO("validusername", "validemail@example.com");
        when(authenticationService.registerUser(anyString(), anyString(), anyString())).thenReturn(ResponseEntity.ok(expectedResponse));

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(validInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo("validusername")))
                .andExpect(jsonPath("$.email", equalTo("validemail@example.com")));
    }

    @Test
    public void testRegisterUser_InvalidUsername_ReturnsBadRequest() throws Exception {
        // Define invalid input with a username that doesn't meet validation rules
        UserRegistrationDTO invalidInput = new UserRegistrationDTO("sh", "ValidPass123!", "validemail@example.com");

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUser_InvalidPassword_ReturnsBadRequest() throws Exception {
        // Define invalid input with a password that doesn't meet validation rules
        UserRegistrationDTO invalidInput = new UserRegistrationDTO("validusername", "invalidpassword", "validemail@example.com");

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    // ... Add more test cases for other validation scenarios

    // Utility method to convert objects to JSON
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
