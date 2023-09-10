package com.hugorithm.hopfencraft.integration;

import com.hugorithm.hopfencraft.config.TestConfig;
import com.hugorithm.hopfencraft.dto.*;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void CreateProduct_ValidInput_ReturnsCreated() {
        //Login
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

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

        assertEquals(HttpStatus.OK, response2.getStatusCode());

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
        // Create a ProductDTO with invalid input
        //Login
        LoginDTO login = new LoginDTO("admin", "Password123!");

        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/auth/login",
                login,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        String jwt = loginResponse.getBody().getJwt();


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
        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput = new ProductRegistrationDTO("Test Product", "Test description", "Test brand", 10, new BigDecimal("19.99"));

        ResponseEntity<ProductDTO> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                validInput,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ProductDTO createdProduct = createResponse.getBody();
        assertNotNull(createdProduct);

        Long productId = createdProduct.getProductId();

        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/" + productId,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Product", responseBody.getName());
        assertEquals("Test description", responseBody.getDescription());
        assertEquals("Test brand", responseBody.getBrand());
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
        // Create some ProductDTOs for testing
        ProductRegistrationDTO product1 = new ProductRegistrationDTO(
                "Product 1",
                "Description 1",
                "Brand 1",
                5,
                new BigDecimal("10.99")
        );
        ProductRegistrationDTO product2 = new ProductRegistrationDTO(
                "Product 2",
                "Description 2",
                "Brand 2",
                8,
                new BigDecimal("15.99")
        );
        ProductRegistrationDTO product3 = new ProductRegistrationDTO(
                "Product 3",
                "Description 3",
                "Brand 3",
                3,
                new BigDecimal("7.99")
        );

        restTemplate.postForEntity("http://localhost:" + port + "/product/register", product1, ProductRegistrationDTO.class);
        restTemplate.postForEntity("http://localhost:" + port + "/product/register", product2, ProductRegistrationDTO.class);
        restTemplate.postForEntity("http://localhost:" + port + "/product/register", product3, ProductRegistrationDTO.class);


        ResponseEntity<Page<ProductDTO>> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/products",
                null);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Page<ProductDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(3, responseBody.getSize());
    }

    @Test
    public void UpdateProduct_ValidInput_ReturnsOk() {
        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput = new ProductRegistrationDTO(
                "Test Product",
                "Test description",
                "Test brand",
                10,
                new BigDecimal("19.99")
        );

        ResponseEntity<ProductDTO> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                validInput,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ProductDTO createdProduct = createResponse.getBody();
        assertNotNull(createdProduct);

        Long productId = createdProduct.getProductId();

        // Update the product
        ProductUpdateDTO updatedProduct = new ProductUpdateDTO(
                productId,
                "Updated Product",
                "Updated description",
                "Updated brand",
                20,
                new BigDecimal("29.99")
        );

        restTemplate.put("http://localhost:" + port + "/product/update", updatedProduct);

        // Verify that the product was updated
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/product/" + productId,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Updated Product", responseBody.getName());
        assertEquals("Updated description", responseBody.getDescription());
        assertEquals("Updated brand", responseBody.getBrand());
        assertEquals(20, responseBody.getQuantity());
        assertEquals(new BigDecimal("29.99"), responseBody.getPrice());
    }

    @Test
    public void UpdateProduct_InvalidInput_ReturnsBadRequest() {
        // Create a ProductDTO for testing
        ProductRegistrationDTO validInput = new ProductRegistrationDTO(
                "Test Product",
                "Test description",
                "Test brand",
                10,
                new BigDecimal("19.99")
        );

        ResponseEntity<ProductDTO> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/product/register",
                validInput,
                ProductDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ProductDTO createdProduct = createResponse.getBody();
        assertNotNull(createdProduct);

        Long productId = createdProduct.getProductId();

        // Attempt to update the product with invalid input
        ProductUpdateDTO invalidInput = new ProductUpdateDTO(
                productId,
                "Short",
                "Test description",
                "Test brand",
                -5,
                new BigDecimal("19.99")
        );

        restTemplate.put("http://localhost:" + port + "/product/update", invalidInput);

        // Verify that the product was not updated due to invalid input
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/products/" + productId,
                ProductDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ProductDTO updatedProduct = response.getBody();
        assertNotNull(updatedProduct);

        // Assert that the product details are still the same as before the update
        assertEquals(createdProduct.getName(), updatedProduct.getName());
        assertEquals(createdProduct.getDescription(), updatedProduct.getDescription());
        assertEquals(createdProduct.getBrand(), updatedProduct.getBrand());
        assertEquals(createdProduct.getQuantity(), updatedProduct.getQuantity());
        assertEquals(createdProduct.getPrice(), updatedProduct.getPrice());

    }
}