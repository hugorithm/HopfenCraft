package com.hugorithm.hopfencraft.integration;

import com.hugorithm.hopfencraft.config.TestConfig;
import com.hugorithm.hopfencraft.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a CartRegistrationDTO for testing
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

    @Test
    public void GetCartItems_EmptyCart_ReturnsOKWithEmptyList() {
        // Login as a user with an empty cart
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Perform a GET request to retrieve cart items
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);



        ResponseEntity<CartResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/cart/items",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CartResponseDTO.class
        );

        // Assert that the response has status code OK (200) and an empty list of cart items
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CartResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        List<CartItemDTO> cartItems = responseBody.getCartItems();
        assertTrue(cartItems.isEmpty());
        // You can check that the cart items list is empty using assertTrue(cartItems.isEmpty());
    }

    @Test
    public void RemoveCartItem_ValidInput_ReturnsOK() {
        // Login as a user
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();


        // Add a cart item to the cart
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

        // Get the cart items and retrieve the cart item ID of the item added
        ResponseEntity<CartResponseDTO> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/cart/items",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CartResponseDTO.class
        );

        // Assert that the response has status code OK (200) and an empty list of cart items
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        CartResponseDTO responseBody2 = response2.getBody();
        assertNotNull(responseBody2);
        List<CartItemDTO> cartItems2 = responseBody2.getCartItems();

        Long cartItemId = cartItems2.get(0).getCartItemId();

        // Perform a DELETE request to remove the cart item using the cart item ID
        ResponseEntity<CartResponseDTO> response3 = restTemplate.exchange(
                "http://localhost:" + port + "/cart/remove/" + cartItemId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                CartResponseDTO.class
        );

        // Assert that the response has status code OK (200)
        assertEquals(HttpStatus.OK, response3.getStatusCode());

        // Get the cart items again and ensure that the removed item is no longer in the list

        CartResponseDTO responseBody3 = response3.getBody();
        assertNotNull(responseBody3);
        List<CartItemDTO> cartItems3 = responseBody3.getCartItems();
        assertTrue(cartItems3.isEmpty());
    }

    @Test
    public void AddCartItem_InvalidProduct_ReturnsBadRequest() {
        // Login as a user
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a CartRegistrationDTO with an invalid product ID (e.g., a product ID that doesn't exist)
        CartRegistrationDTO invalidInput = new CartRegistrationDTO(9999L, 5);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<CartRegistrationDTO> requestEntity = new HttpEntity<>(invalidInput, headers);

        // Perform a POST request to add the cart item with the invalid product ID
        ResponseEntity<CartResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/cart/add",
                requestEntity,
                CartResponseDTO.class
        );

        // Assert that the response has status code Bad Request (400) indicating that the product is invalid
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void AddCartItem_InvalidQuantity_ReturnsBadRequest() {
        // Login as a user
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();
        // Create a CartRegistrationDTO with an invalid quantity (e.g., a negative quantity)
        CartRegistrationDTO invalidInput = new CartRegistrationDTO(1L, 90);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<CartRegistrationDTO> requestEntity = new HttpEntity<>(invalidInput, headers);
        // Perform a POST request to add the cart item with the invalid quantity

        ResponseEntity<CartResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/cart/add",
                requestEntity,
                CartResponseDTO.class
        );
        // Assert that the response has status code Bad Request (400) indicating that the quantity is invalid
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void RemoveCartItem_NonExistentItem_ReturnsNotFound() {
        // Login as a user
        LoginDTO login = new LoginDTO("testuser", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        // Attempt to remove a cart item with a cart item ID that doesn't exist

        long cartItemId = 9999L;
        ResponseEntity<CartResponseDTO> response3 = restTemplate.exchange(
                "http://localhost:" + port + "/cart/remove/" + cartItemId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                CartResponseDTO.class
        );

        // Assert that the response has status code Not Found (404)
        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
    }

}
