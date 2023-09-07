package com.hugorithm.hopfencraft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugorithm.hopfencraft.dto.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class AuthenticationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void RegisterUser_ValidInput_ReturnsOk() throws Exception {
        // Define valid input data
        UserRegistrationDTO validInput = new UserRegistrationDTO("validusername", "ValidPass123!", "validemail@example.com");

        // Define the expected response from the service
        UserRegistrationResponseDTO expectedResponse = new UserRegistrationResponseDTO("validusername", "validemail@example.com");

        given(authenticationService.registerUser(validInput.getUsername(), validInput.getPassword(), validInput.getEmail()))
                .willReturn(ResponseEntity.ok(expectedResponse));

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(validInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo("validusername")))
                .andExpect(jsonPath("$.email", equalTo("validemail@example.com")));
    }

    @Test
    public void RegisterUser_InvalidUsername_ReturnsBadRequest() throws Exception {
        // Define invalid input with a username that doesn't meet validation rules
        UserRegistrationDTO invalidInput = new UserRegistrationDTO("nn", "ValidPass123!", "validemail@example.com");

        given(authenticationService.registerUser(invalidInput.getUsername(), invalidInput.getPassword(), invalidInput.getEmail()))
                .willReturn(ResponseEntity.badRequest().build());

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        // Define invalid input with a email that doesn't meet validation rules
        UserRegistrationDTO invalidInput = new UserRegistrationDTO("validusername", "ValidPass123!", "invalidemail");

        given(authenticationService.registerUser(invalidInput.getUsername(), invalidInput.getPassword(), invalidInput.getEmail()))
                .willReturn(ResponseEntity.badRequest().build());

        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterUser_InvalidPassword_ReturnsBadRequest() throws Exception {
        // Define invalid input with a password that doesn't meet validation rules
        UserRegistrationDTO invalidInput = new UserRegistrationDTO("validusername", "invalidpassword", "validemail@example.com");
        given(authenticationService.registerUser(invalidInput.getUsername(), invalidInput.getPassword(), invalidInput.getEmail())).willReturn(ResponseEntity.badRequest().build());
        // Perform the POST request
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }


    // Utility method to convert objects to JSON
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
