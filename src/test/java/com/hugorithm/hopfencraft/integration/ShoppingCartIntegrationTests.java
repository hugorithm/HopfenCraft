package com.hugorithm.hopfencraft.integration;

import com.hugorithm.hopfencraft.config.TestConfig;
import com.hugorithm.hopfencraft.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {TestConfig.class})
public class ShoppingCartIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void AddCartItem_ValidInput_ReturnsCreated() {
        //Login
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        String jwt = loginResponse.getBody().getJwt();

        // Create a ProductDTO for testing
        CartRegistrationDTO validInput = new CartRegistrationDTO(1L, 5);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<CartRegistrationDTO> requestEntity = new HttpEntity<>(validInput, headers);

        ResponseEntity<CartResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/cart/add",
                requestEntity,
                CartResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        CartResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);

        List<CartItemDTO> cartItems = responseBody.getCartItems();

        for (CartItemDTO cartItemDTO : cartItems) {
            assertEquals(1L, cartItemDTO.getCartItemId());
            assertEquals(5, cartItemDTO.getQuantity());
            assertNotNull(cartItemDTO.getProduct());
        }

    }
}
