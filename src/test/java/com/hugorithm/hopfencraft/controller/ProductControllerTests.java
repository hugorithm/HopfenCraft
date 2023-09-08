package com.hugorithm.hopfencraft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.dto.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
                        .content(asJsonString(validInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", equalTo( expectedResponse.getBrand())))
                .andExpect(jsonPath("$.name", equalTo(expectedResponse.getName())))
                .andExpect(jsonPath("$.description", equalTo(expectedResponse.getDescription())))
                .andExpect(jsonPath("$.quantity", equalTo(expectedResponse.getQuantity())))
                .andExpect(jsonPath("$.price", equalTo(expectedResponse.getPrice().toString())));


    }

    // Utility method to convert objects to JSON
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }


}
