package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.dto.product.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.dto.product.ProductUpdateDTO;
import com.hugorithm.hopfencraft.enums.Currency;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.model.ProductImage;
import com.hugorithm.hopfencraft.service.ProductService;
import com.hugorithm.hopfencraft.utils.JsonToStringConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    private Jwt mockJwt;

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductControllerTests.class);

    @Test
    public void RegisterProduct_ValidInput_ReturnsCreated() throws Exception {
        // Define valid input data

        BigDecimal price = new BigDecimal("2.39");
        ProductRegistrationDTO validInput = new ProductRegistrationDTO();
        validInput.setBrand("Paulaner");
        validInput.setName("Paulaner Weizen");
        validInput.setDescription("Weiss");
        validInput.setQuantity(10);
        validInput.setPrice(price);
        validInput.setCurrency(Currency.EUR);

        // Define the expected response from the service
        ProductDTO expectedResponse = new ProductDTO(
                1L,
                "Paulaner",
                "Paulaner Weizen",
                "Weiss",
                10,
                price,
                Currency.EUR,
                LocalDateTime.now()
        );

        // Mock the productService.registerProduct method to return the expected response
        when(productService.registerProduct(
                same(mockJwt),
                eq(validInput)
        )).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(expectedResponse));

        // Perform the POST request using mockMvc
        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockJwt)
                        .content(JsonToStringConverter.asJsonString(validInput)))
                .andExpect(status().isCreated()) // Expect a 201 Created status
                .andExpect(jsonPath("$.brand").value(expectedResponse.getBrand()))
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.description").value(expectedResponse.getDescription()))
                .andExpect(jsonPath("$.quantity").value(expectedResponse.getQuantity()))
                .andExpect(jsonPath("$.price").value(expectedResponse.getPrice().toString()))
                .andExpect(jsonPath("$.productId").isNumber());

        // Verify that the productService.registerProduct method was called with the expected arguments
        verify(productService).registerProduct(
                same(mockJwt),
                eq(validInput)
        );
    }

    @Test
    public void RegisterProduct_InvalidInput_ReturnsBadRequest() throws Exception {
        // Define invalid input data (missing required fields)
        ProductRegistrationDTO invalidInput = new ProductRegistrationDTO();

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());


        // Define invalid input data (missing required fields)
        ProductRegistrationDTO invalidInput2 = new ProductRegistrationDTO();
        invalidInput2.setBrand("aa");
        invalidInput2.setName("bb");
        invalidInput2.setDescription("cc");
        invalidInput2.setQuantity(10);
        invalidInput2.setPrice(new BigDecimal("-2.6"));
        invalidInput2.setCurrency(Currency.EUR);


        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput2)))
                .andExpect(status().isBadRequest());

        // Define invalid input data (missing required fields)
        ProductRegistrationDTO invalidInput3 = new ProductRegistrationDTO();
        invalidInput3.setBrand("aa");
        invalidInput3.setName("bb");
        invalidInput3.setDescription("cc");
        invalidInput3.setQuantity(-10);
        invalidInput3.setPrice(new BigDecimal("2.6"));
        invalidInput3.setCurrency(Currency.EUR);



        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterProduct_DuplicateProduct_ReturnsConflict() throws Exception {
        // Define input data for a product that already exists

        ProductRegistrationDTO existingProduct = new ProductRegistrationDTO();
        existingProduct.setBrand("Paulaner");
        existingProduct.setName("Paulaner Weizen");
        existingProduct.setDescription("Weiss");
        existingProduct.setQuantity(10);
        existingProduct.setPrice(new BigDecimal("2.39"));
        existingProduct.setCurrency(Currency.EUR);


        // Simulate the ProductService returning a conflict response
        when(productService.registerProduct(
                same(mockJwt),
                eq(existingProduct)
        )).thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).build());

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockJwt)
                        .content(JsonToStringConverter.asJsonString(existingProduct)))
                .andExpect(status().isConflict());
    }

    @Test
    public void RegisterProduct_InvalidPrice_ReturnsBadRequest() throws Exception {
        // Define invalid input data with a negative price

        ProductRegistrationDTO invalidInput = new ProductRegistrationDTO();
        invalidInput.setBrand("Invalid Product");
        invalidInput.setName("Invalid Description");
        invalidInput.setDescription("Category");
        invalidInput.setQuantity(10);
        invalidInput.setPrice(new BigDecimal("-1.0"));
        invalidInput.setCurrency(Currency.EUR);

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());

        // Define invalid input data with a zero price
        ProductRegistrationDTO invalidInput2 = new ProductRegistrationDTO();
        invalidInput.setBrand("Invalid Product");
        invalidInput.setName("Invalid Description");
        invalidInput.setDescription("Category");
        invalidInput.setQuantity(10);
        invalidInput.setPrice(BigDecimal.ZERO);
        invalidInput.setCurrency(Currency.EUR);

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void GetProducts_Paginated_ReturnsPageOfProducts() throws Exception {
        // Define test data with multiple products
        Product p1 = new Product(1L, "Paulaner", "Paulaner", "Weiss", 10, new BigDecimal("2.39"));
        Product p2 = new Product(2L, "Franziskaner", "Franziskaner", "Weiss", 31, new BigDecimal("2.29"));
        Product p3 = new Product(3L, "La Choufe", "La Choufe", "Belgium Gold", 14, new BigDecimal("3.57"));
        Product p4 = new Product(4L, "Benediktiner", "Benediktiner", "Weiss", 10, new BigDecimal("2.39"));
        Product p5 = new Product(5L, "Spaten", "Spaten", "Weiss", 31, new BigDecimal("2.29"));
        Product p6 = new Product(6L, "Ayinger", "Ayinger", "Belgium Gold", 14, new BigDecimal("3.57"));
        Product p7 = new Product(7L, "Krombacher", "Krombacher", "Weiss", 10, new BigDecimal("2.39"));
        Product p8 = new Product(8L, "Erdinger", "Erdinger", "Weiss", 31, new BigDecimal("2.29"));
        Product p9 = new Product(9L, "Augistiner", "Augistiner", "Belgium Gold", 14, new BigDecimal("3.57"));
        Product p10 = new Product(10L, "Kapunziner", "Kapunziner", "Weiss", 10, new BigDecimal("2.39"));
        Product p11 = new Product(11L, "Munchener", "Munchener", "Weiss", 31, new BigDecimal("2.29"));
        Product p12 = new Product(12L, "La Choufe 2", "La Choufe 2", "Belgium Gold", 14, new BigDecimal("3.57"));

        List<Product> products = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);

        // Simulate the ProductService returning a page of products
        Page<Product> productPage = new PageImpl<>(products);
        Page<ProductDTO> dtoPage = productPage.map(p -> new ProductDTO(
                p.getProductId(),
                p.getBrand(),
                p.getName(),
                p.getDescription(),
                p.getStockQuantity(),
                p.getPrice(),
                Product.getCurrency(),
                p.getRegisterDateTime()
        ));

        when(productService.getProducts(Mockito.any(Pageable.class), Mockito.any())).thenReturn(ResponseEntity.ok(dtoPage));

        mockMvc.perform(get("/product/products")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(products.size())))
                .andExpect(jsonPath("$.content[0].productId", notNullValue()))
                .andExpect(jsonPath("$.content[0].brand", notNullValue()))
                .andExpect(jsonPath("$.content[0].name", notNullValue()))
                .andExpect(jsonPath("$.content[0].description", notNullValue()))
                .andExpect(jsonPath("$.content[0].quantity", notNullValue()))
                .andExpect(jsonPath("$.content[0].price", notNullValue()));
    }

    @Test
    public void GetProductById_ExistingProduct_ReturnsProduct() throws Exception {
        // Define an existing product

        ApplicationUser user = new ApplicationUser();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        Long productId = 1L;
        Product existingProduct = new Product(
                productId,
                "SKU01",
                "Existing Brand",
                "Existing Product",
                "Existing BrandName",
                "Existing Description",
                5,
                new BigDecimal("4.99"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                user,
                new ProductImage()
        );

        when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));

        mockMvc.perform(get("/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", equalTo(existingProduct.getProductId().intValue())))
                .andExpect(jsonPath("$.brand", equalTo(existingProduct.getBrand())))
                .andExpect(jsonPath("$.name", equalTo(existingProduct.getName())))
                .andExpect(jsonPath("$.description", equalTo(existingProduct.getDescription())))
                .andExpect(jsonPath("$.quantity", equalTo(existingProduct.getStockQuantity())))
                .andExpect(jsonPath("$.price", equalTo(existingProduct.getPrice().toString())));
    }

    @Test
    public void GetProductById_NonExistingProduct_ReturnsNotFound() throws Exception {
        // Define a non-existing product ID
        Long nonExistingProductId = 999L;

        when(productService.findById(nonExistingProductId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/product/{productId}", nonExistingProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void UpdateProduct_ValidInput_ReturnsOk() throws Exception {
        // Define valid input data for updating a product
        Long productId = 1L;
        LocalDateTime dt = LocalDateTime.now();
        ProductUpdateDTO validInput = new ProductUpdateDTO(
                1L,
                "Updated Brand",
                "Updated Product",
                "Updated Description",
                20,
                new BigDecimal("9.99")
        );

        // Define the expected updated product response
        ProductDTO expectedUpdatedProduct = new ProductDTO(
                productId,
                "Updated Brand",
                "Updated Product",
                "Updated Description",
                20,
                new BigDecimal("9.99"),
                Currency.EUR,
                dt
        );

        when(productService.updateProduct(validInput)).thenReturn(ResponseEntity.ok(expectedUpdatedProduct));

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(validInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", equalTo(expectedUpdatedProduct.getBrand())))
                .andExpect(jsonPath("$.name", equalTo(expectedUpdatedProduct.getName())))
                .andExpect(jsonPath("$.description", equalTo(expectedUpdatedProduct.getDescription())))
                .andExpect(jsonPath("$.quantity", equalTo(expectedUpdatedProduct.getQuantity())))
                .andExpect(jsonPath("$.price", equalTo(expectedUpdatedProduct.getPrice().toString())));
    }

    @Test
    public void UpdateProduct_InvalidQuantity_ReturnsBadRequest() throws Exception {
        // Define invalid input data for updating a product (e.g., empty name)
        Long productId = 1L;
        ProductUpdateDTO invalidInput = new ProductUpdateDTO(
                productId,
                "Update Product Brand",
                "Updated Product",
                "Updated Description",
                -20, //negative quantity
                new BigDecimal("9.99")
        );

        when(productService.updateProduct(invalidInput)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void UpdateProduct_InvalidEmptyInput_ReturnsBadRequest() throws Exception {
        // Define invalid input data for updating a product (e.g., empty name)
        Long productId = 1L;
        ProductUpdateDTO invalidInput = new ProductUpdateDTO();
        invalidInput.setProductId(productId);
        invalidInput.setQuantity(10);
        invalidInput.setPrice(new BigDecimal("2.50"));

        when(productService.updateProduct(invalidInput)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void RemoveProduct_ValidInput_ReturnsOk() throws Exception {
        // Define a valid product ID to be removed
        Long productId = 1L;

        // Simulate a successful removal by the productService
        when(productService.deleteProduct(productId)).thenReturn(ResponseEntity.ok("Product removed successfully"));

        mockMvc.perform(delete("/product/remove/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(productId)))
                .andExpect(status().isOk());
    }

    @Test
    public void RemoveProduct_NonExistingProduct_ReturnsNotFound() throws Exception {
        // Define a non-existing product ID to be removed
        Long nonExistingProductId = 999L;

        // Simulate productService returning a not found response
        when(productService.deleteProduct(nonExistingProductId)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/product/remove/{nonExistingProductId}", nonExistingProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(nonExistingProductId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void RemoveProduct_ErrorInRemoving_ReturnsInternalServerError() throws Exception {
        // Define a product ID that will result in an error during removal
        Long productIdWithError = 2L;

        // Simulate productService returning an internal server error response
        when(productService.deleteProduct(productIdWithError))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while removing product"));

        mockMvc.perform(delete("/product/remove/{productIdWithError}", productIdWithError)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(productIdWithError)))
                .andExpect(status().isInternalServerError());
    }

}
