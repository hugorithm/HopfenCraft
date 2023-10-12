package com.hugorithm.hopfencraft.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetRequestDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetResponseDTO;
import com.hugorithm.hopfencraft.service.UserService;
import com.hugorithm.hopfencraft.utils.JsonToStringConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private Jwt mockJwt;

    @BeforeEach
    public void setUp() {
        // Create a mock Jwt for testing
        mockJwt = Mockito.mock(Jwt.class);
    }

    //TODO: Properly mock this, Instantiate UserService and mock functions accordingly
    @Test
    public void SendPasswordResetRequest_ValidInput_ReturnsOK() throws Exception {
        // Mock the behavior of your userService to return a ResponseEntity
        ResponseEntity<PasswordResetResponseDTO> responseEntity = ResponseEntity.status(HttpStatus.OK)
                .body(new PasswordResetResponseDTO("Request sent successfully"));

        when(userService.sendPasswordResetRequest(Mockito.any(PasswordResetRequestDTO.class))).thenReturn(responseEntity);
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO("testuser");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/reset-password-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ResetPasswordRequest_ValidInput_ReturnsOk() throws Exception {
        // Mock the behavior of your userService to return a ResponseEntity

        when(userService.showPasswordResetForm(Mockito.anyString())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.get("/user/reset-password")
                        .param("token", "mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ResetPassword_ValidInput_ReturnsOk() throws Exception {
        // Create a PasswordResetDTO for testing
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO("newPassword123!", "newPassword123!");

        // Mock the behavior of your userService to return a success response
        String resetToken = "resetToken"; // Replace with an actual reset token
        when(userService.resetPassword(resetToken, passwordResetDTO))
                .thenReturn(ResponseEntity.ok().build());

        // Convert the DTO to JSON
        String requestBody = JsonToStringConverter.asJsonString(passwordResetDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/reset-password")
                        .header("Authorization", "Bearer mockToken")
                        .param("token", resetToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
