package com.hugorithm.hopfencraft.integration;

import com.hugorithm.hopfencraft.config.TestConfig;
import com.hugorithm.hopfencraft.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {TestConfig.class})
public class ProductIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void CreateProduct_ValidInput_ReturnsCreated() {
        //Login
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput3 = new ProductRegistrationDTO(
                "Test Brand",
                "Test Name",
                "Test Description",
                10,
                new BigDecimal("19.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<ProductRegistrationDTO> requestEntity = new HttpEntity<>(validInput3, headers);

        ResponseEntity<ProductDTO> response2 = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                requestEntity,
                ProductDTO.class
        );

        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        ProductDTO responseBody2 = response2.getBody();
        assertNotNull(responseBody2);

        assertEquals("Test Brand", responseBody2.getBrand());
        assertEquals("Test Name", responseBody2.getName());
        assertEquals("Test Description", responseBody2.getDescription());
        assertEquals(10, responseBody2.getQuantity());
        assertEquals(new BigDecimal("19.99"), responseBody2.getPrice());
    }

    @Test
    public void CreateProduct_InvalidInput_ReturnsBadRequest() {
        //Login
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a ProductDTO with invalid input
        ProductRegistrationDTO invalidInput = new ProductRegistrationDTO(
                "Test Brand",
                "Test Name",
                "Test Description",
                -5,
                new BigDecimal("19.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<ProductRegistrationDTO> requestEntity = new HttpEntity<>(invalidInput, headers);

        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                requestEntity,
                ProductDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ProductDTO responseBody = response.getBody();
        assertNull(responseBody);

    }

    @Test
    public void GetProductById_ValidId_ReturnsOk() {
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput = new ProductRegistrationDTO(
                "Test Brand",
                "Test Name",
                "Test Description",
                10,
                new BigDecimal("19.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<ProductRegistrationDTO> requestEntity = new HttpEntity<>(validInput, headers);


        ResponseEntity<ProductDTO> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                requestEntity,
                ProductDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ProductDTO createdProduct = createResponse.getBody();
        assertNotNull(createdProduct);

        Long productId = createdProduct.getProductId();

        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/" + productId,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Brand", responseBody.getBrand());
        assertEquals("Test Name", responseBody.getName());
        assertEquals("Test Description", responseBody.getDescription());
        assertEquals(10, responseBody.getQuantity());
        assertEquals(new BigDecimal("19.99"), responseBody.getPrice());
    }

    @Test
    public void GetProductById_InvalidId_ReturnsNotFound() {


        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/9999", // Assuming ID 9999 doesn't exist
                ProductDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void GetProducts_ReturnsOk() {
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create some ProductDTOs for testing
        ProductRegistrationDTO product1 = new ProductRegistrationDTO(
                "Brand 1",
                "Name 1",
                "Description 1",
                5,
                new BigDecimal("10.99")
        );
        ProductRegistrationDTO product2 = new ProductRegistrationDTO(
                "Brand 2",
                "Name 2",
                "Description 2",
                8,
                new BigDecimal("15.99")
        );
        ProductRegistrationDTO product3 = new ProductRegistrationDTO(
                "Brand 3",
                "Name 3",
                "Description 3",
                3,
                new BigDecimal("7.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<ProductRegistrationDTO> requestEntity1 = new HttpEntity<>(product1, headers);
        HttpEntity<ProductRegistrationDTO> requestEntity2 = new HttpEntity<>(product2, headers);
        HttpEntity<ProductRegistrationDTO> requestEntity3 = new HttpEntity<>(product3, headers);


        restTemplate.postForEntity("http://localhost:" + port + "/product/register", requestEntity1, ProductRegistrationDTO.class);
        restTemplate.postForEntity("http://localhost:" + port + "/product/register", requestEntity2, ProductRegistrationDTO.class);
        restTemplate.postForEntity("http://localhost:" + port + "/product/register", requestEntity3, ProductRegistrationDTO.class);


        ResponseEntity<Page<ProductDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/product/products",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Page<ProductDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Page<ProductDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        // 15 here is because TestConfig runs DataInitialization which creates 12 Products. Here we add 3 more so 12 + 3 = 15
        assertEquals(15, responseBody.getTotalElements());


    }

    @Test
    public void UpdateProduct_ValidInput_ReturnsOk() {
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput = new ProductRegistrationDTO(
                "Test Brand",
                "Test Name",
                "Test Description",
                10,
                new BigDecimal("19.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<ProductRegistrationDTO> requestEntity = new HttpEntity<>(validInput, headers);

        ResponseEntity<ProductDTO> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                requestEntity,
                ProductDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ProductDTO createdProduct = createResponse.getBody();
        assertNotNull(createdProduct);

        Long productId = createdProduct.getProductId();

        // Update the product
        ProductUpdateDTO updatedProduct = new ProductUpdateDTO(
                productId,
                "Updated Brand",
                "Updated Name",
                "Updated Description",
                20,
                new BigDecimal("29.99")
        );

        HttpEntity<ProductUpdateDTO> requestEntityUpdate = new HttpEntity<>(updatedProduct, headers);

        restTemplate.exchange(
                "http://localhost:" + port + "/product/update",
                HttpMethod.PUT,
                requestEntityUpdate,
                ProductDTO.class
        );

        // Verify that the product was updated
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/" + productId,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Updated Brand", responseBody.getBrand());
        assertEquals("Updated Name", responseBody.getName());
        assertEquals("Updated Description", responseBody.getDescription());
        assertEquals(20, responseBody.getQuantity());
        assertEquals(new BigDecimal("29.99"), responseBody.getPrice());
    }

    @Test
    public void UpdateProduct_InvalidInput_ReturnsBadRequest() {
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String jwt = loginResponse.getBody().getJwt();

        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput = new ProductRegistrationDTO(
                "Test Brand",
                "Test Name",
                "Test Description",
                10,
                new BigDecimal("19.99")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<ProductRegistrationDTO> requestEntity = new HttpEntity<>(validInput, headers);

        ResponseEntity<ProductDTO> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                requestEntity,
                ProductDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ProductDTO createdProduct = createResponse.getBody();
        assertNotNull(createdProduct);

        Long productId = createdProduct.getProductId();

        // Attempt to update the product with invalid input
        ProductUpdateDTO invalidInput = new ProductUpdateDTO(
                productId,
                "Test Brand Updated",
                "Test Name Updated",
                "Test Description Updated",
                -5,
                new BigDecimal("-39.99")
        );

        HttpEntity<ProductUpdateDTO> requestEntityUpdate = new HttpEntity<>(invalidInput, headers);

        restTemplate.exchange(
                "http://localhost:" + port + "/product/update",
                HttpMethod.PUT,
                requestEntityUpdate,
                ProductDTO.class
        );

        // Verify that the product was not updated due to invalid input
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/" + productId,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDTO updatedProduct = response.getBody();
        assertNotNull(updatedProduct);

        // Assert that the product details are still the same as before the update
        // In this example only the invalid quantity should be ignored. The rest should be updated
        assertNotEquals(createdProduct.getBrand(), updatedProduct.getBrand());
        assertNotEquals(createdProduct.getName(), updatedProduct.getName());
        assertNotEquals(createdProduct.getDescription(), updatedProduct.getDescription());
        assertEquals(createdProduct.getQuantity(), updatedProduct.getQuantity());
        assertEquals(createdProduct.getPrice(), updatedProduct.getPrice());

    }
}