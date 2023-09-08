package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.dto.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.service.ProductService;
import com.hugorithm.hopfencraft.utils.JsonToStringConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void RegisterProduct_ValidInput_ReturnsOk() throws Exception {
        // Define valid input data

        BigDecimal price = new BigDecimal("2.39");
        ProductRegistrationDTO validInput = new ProductRegistrationDTO(
                "Paulaner",
                "Paulaner Weizen",
                "Weiss",
                10,
                price
        );

        // Define the expected response from the service
        ProductDTO expectedResponse = new ProductDTO(1L,
                "Paulaner",
                "Paulaner Weizen",
                "Weiss",
                10,
                price,
                LocalDateTime.now()
        );

        given(productService.registerProduct(
                validInput.getBrand(),
                validInput.getName(),
                validInput.getDescription(),
                validInput.getQuantity(),
                validInput.getPrice()
        )).willReturn(ResponseEntity.ok(expectedResponse));

        // Perform the POST request
        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(validInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", equalTo( expectedResponse.getBrand())))
                .andExpect(jsonPath("$.name", equalTo(expectedResponse.getName())))
                .andExpect(jsonPath("$.description", equalTo(expectedResponse.getDescription())))
                .andExpect(jsonPath("$.quantity", equalTo(expectedResponse.getQuantity())))
                .andExpect(jsonPath("$.price", equalTo(expectedResponse.getPrice().toString())));


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
        ProductRegistrationDTO invalidInput2 = new ProductRegistrationDTO(
                "aa",
                "bb",
                "cc",
                10,
                new BigDecimal("-2.6")
        );

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput2)))
                .andExpect(status().isBadRequest());

        // Define invalid input data (missing required fields)
        ProductRegistrationDTO invalidInput3 = new ProductRegistrationDTO(
                "aa",
                "bb",
                "cc",
                -10,
                new BigDecimal("2.6")
        );

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(invalidInput3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void RegisterProduct_DuplicateProduct_ReturnsConflict() throws Exception {
        // Define input data for a product that already exists
        ProductRegistrationDTO existingProduct = new ProductRegistrationDTO(
                "Paulaner",
                "Paulaner Weizen",
                "Weiss",
                10,
                new BigDecimal("2.39")
        );

        // Simulate the ProductService returning a conflict response
        given(productService.registerProduct(
                existingProduct.getBrand(),
                existingProduct.getName(),
                existingProduct.getDescription(),
                existingProduct.getQuantity(),
                existingProduct.getPrice()
        )).willReturn(ResponseEntity.status(HttpStatus.CONFLICT).build());

        mockMvc.perform(post("/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonToStringConverter.asJsonString(existingProduct)))
                .andExpect(status().isConflict());
    }


}
