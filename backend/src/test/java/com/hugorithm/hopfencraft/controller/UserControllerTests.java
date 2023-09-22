package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

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
    public void SendPasswordResetRequest_ValidInput_ReturnsOk() throws Exception {
        // Mock the behavior of your userService to return a ResponseEntity
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Request sent successfully");

        when(userService.sendPasswordResetRequest(Mockito.any(Jwt.class))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/reset-password-request")
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ResetPasswordRequest_ValidInput_ReturnsOk() throws Exception {
        // Mock the behavior of your userService to return a ResponseEntity

        when(userService.showPasswordResetForm(Mockito.any(Jwt.class), Mockito.anyString())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.get("/user/reset-password")
                        .header("Authorization", "Bearer mockToken")
                        .param("token", "mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ResetPassword_ValidInput_ReturnsOk() throws Exception {
        // Create a PasswordResetDTO for testing
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser(
                "user1",
                "Password123!",
                "email@example.com",
                roles,
                "Test",
                "test",
                AuthProvider.LOCAL);
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO("newPassword123!", "newPassword123!");

        // Mock the behavior of your userService to return a success response
        String resetToken = "resetToken"; // Replace with an actual reset token
        when(userService.resetPassword(mockJwt, resetToken, passwordResetDTO))
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
