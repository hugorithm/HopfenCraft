package com.hugorithm.hopfencraft.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTests {
    @Autowired
    private MockMvc mockMvc;
    //TODO: Create an admin service
    //@MockBean
    //private AdminService userService;

    private Jwt mockJwt;

    @BeforeEach
    public void setUp() {
        // Create a mock Jwt for testing
        mockJwt = Mockito.mock(Jwt.class);
    }
}
