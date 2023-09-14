package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterProduct_ValidInput_ReturnsOk() {
        // Implement test logic for a successful product registration.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        // Create a valid product DTO
        ProductDTO validProductDTO = new ProductDTO(null, "TestBrand", "TestName", "TestDescription", 10, new BigDecimal("19.99"), null);

        // Mock the product repository to return an empty Optional, indicating the product does not exist
        when(productRepository.findProductByName("TestName")).thenReturn(Optional.empty());

        when(jwtService.getUserFromJwt(any())).thenReturn(user);

        // Mock the product repository to save the product and return it
        Product savedProduct = new Product("TestBrand", "TestName", "TestDescription", 10, new BigDecimal("19.99"), user);
        when(productRepository.save(any())).thenReturn(savedProduct);

        // Call the service method
        ResponseEntity<ProductDTO> response = productService.registerProduct(
                mock(Jwt.class),
                validProductDTO.getBrand(),
                validProductDTO.getName(),
                validProductDTO.getDescription(),
                validProductDTO.getQuantity(),
                validProductDTO.getPrice()
        );

        // Assert the expected behavior
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ProductDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("TestBrand", responseBody.getBrand());
        assertEquals("TestName", responseBody.getName());
        assertEquals("TestDescription", responseBody.getDescription());
        assertEquals(10, responseBody.getQuantity());
        assertEquals(new BigDecimal("19.99"), responseBody.getPrice());
    }

    @Test
    public void testRegisterProduct_ProductAlreadyExists_ReturnsConflict() {
        // Implement test logic for product registration when the product already exists.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        // Create a valid product DTO
        ProductDTO validProductDTO = new ProductDTO(null, "TestBrand", "TestName", "TestDescription", 10, new BigDecimal("19.99"), null);

        // Mock the product repository to return a non-empty Optional, indicating the product already exists
        when(productRepository.findProductByName("TestName")).thenReturn(Optional.of(new Product()));
        when(jwtService.getUserFromJwt(any())).thenReturn(user);
        // Call the service method
        ResponseEntity<ProductDTO> response = productService.registerProduct(
                mock(Jwt.class),
                validProductDTO.getBrand(),
                validProductDTO.getName(),
                validProductDTO.getDescription(),
                validProductDTO.getQuantity(),
                validProductDTO.getPrice()
        );

        // Assert the expected behavior
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_ValidInput_ReturnsOk() {
        // Implement test logic for a successful product update.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        Long productId = 1L;
        Product existingProduct = new Product("TestBrand", "TestName", "TestDescription", 5, new BigDecimal("19.99"), user);
        existingProduct.setProductId(productId);

        // Mock the product repository to return the existing product
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Call the service method to update the product
        ResponseEntity<ProductDTO> response = productService.updateProduct(
                productId,
                "UpdatedBrand",
                "UpdatedName",
                "UpdatedDescription",
                10,
                new BigDecimal("29.99")
        );

        // Assert the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("UpdatedBrand", responseBody.getBrand());
        assertEquals("UpdatedName", responseBody.getName());
        assertEquals("UpdatedDescription", responseBody.getDescription());
        assertEquals(10, responseBody.getQuantity());
        assertEquals(new BigDecimal("29.99"), responseBody.getPrice());
    }

    @Test
    public void testUpdateProduct_ProductNotFound_ReturnsNotFound() {
        // Implement test logic when the product to update does not exist.
        Long productId = 1L;

        // Mock the product repository to return an empty Optional, indicating the product does not exist
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the service method to update the product
        ResponseEntity<ProductDTO> response = productService.updateProduct(
                productId,
                "UpdatedBrand",
                "UpdatedName",
                "UpdatedDescription",
                10,
                new BigDecimal("29.99")
        );

        // Assert the expected behavior
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_InvalidQuantity_ReturnsBadRequest() {
        // Implement test logic for updating a product with an invalid quantity.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        Long productId = 1L;
        Product existingProduct = new Product("TestBrand", "TestName", "TestDescription", 5, new BigDecimal("19.99"), user);
        existingProduct.setProductId(productId);

        // Mock the product repository to return the existing product
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Call the service method to update the product with a negative quantity
        ResponseEntity<ProductDTO> response = productService.updateProduct(
                productId,
                "UpdatedBrand",
                "UpdatedName",
                "UpdatedDescription",
                -5,
                new BigDecimal("29.99")
        );

        // Assert the expected behavior
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_InvalidPrice_ReturnsBadRequest() {
        // Implement test logic for updating a product with an invalid price.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        Long productId = 1L;
        Product existingProduct = new Product("TestBrand", "TestName", "TestDescription", 5, new BigDecimal("19.99"), user);
        existingProduct.setProductId(productId);

        // Mock the product repository to return the existing product
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Call the service method to update the product with a negative price
        ResponseEntity<ProductDTO> response = productService.updateProduct(
                productId,
                "UpdatedBrand",
                "UpdatedName",
                "UpdatedDescription",
                10,
                new BigDecimal("-5.00")
        );

        // Assert the expected behavior
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRemoveProduct_ValidProductId_ReturnsOk() {
        // Implement test logic for removing a product with a valid product ID.
        Long productId = 1L;

        // Mock the product repository to return the existing product
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));

        // Call the service method to remove the product
        ResponseEntity<String> response = productService.removeProduct(productId);

        // Assert the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product removed successfully", response.getBody());
    }

    @Test
    public void testRemoveProduct_ProductNotFound_ReturnsNotFound() {
        // Implement test logic when the product to remove does not exist.
        Long productId = 1L;

        // Mock the product repository to return an empty Optional, indicating the product does not exist
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the service method to remove the product
        ResponseEntity<String> response = productService.removeProduct(productId);

        // Assert the expected behavior
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindAllProducts_ReturnsPageOfProducts() {
        // Implement test logic for retrieving all products with pagination.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        // Mock data initialization to return a sample page of products
        Pageable pageable = Pageable.ofSize(5).withPage(0);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(
                new Product("Brand1", "Product1", "Description1", 5, new BigDecimal("19.99"), user),
                new Product("Brand2", "Product2", "Description2", 10, new BigDecimal("29.99"), user),
                new Product("Brand3", "Product3", "Description3", 15, new BigDecimal("39.99"), user)
        )));

        // Call the service method to retrieve all products
        Page<Product> productsPage = productService.findAll(pageable);

        // Assert the expected behavior
        assertEquals(3, productsPage.getTotalElements());
        assertEquals(3, productsPage.getContent().size());
    }

    @Test
    public void testFindProductById_ValidProductId_ReturnsProduct() {
        // Implement test logic for retrieving a product by its valid product ID.
        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        Long productId = 1L;
        Product existingProduct = new Product("Brand", "Product", "Description", 5, new BigDecimal("19.99"), user);
        existingProduct.setProductId(productId);

        // Mock the product repository to return the existing product
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Call the service method to find the product by ID
        Optional<Product> foundProduct = productService.findById(productId);

        // Assert the expected behavior
        assertTrue(foundProduct.isPresent());
        assertEquals(existingProduct, foundProduct.get());
    }

    @Test
    public void testFindProductById_ProductNotFound_ReturnsEmptyOptional() {
        // Implement test logic when trying to retrieve a non-existing product by ID.
        Long productId = 1L;

        // Mock the product repository to return an empty Optional, indicating the product does not exist
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call the service method to find the product by ID
        Optional<Product> foundProduct = productService.findById(productId);

        // Assert the expected behavior
        assertTrue(foundProduct.isEmpty());
    }
    // Add more test cases for other methods in the ProductService class.
}

