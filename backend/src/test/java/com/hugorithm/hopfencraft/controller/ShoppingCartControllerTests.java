package com.hugorithm.hopfencraft.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugorithm.hopfencraft.dto.cart.CartItemDTO;
import com.hugorithm.hopfencraft.dto.cart.CartRegistrationDTO;
import com.hugorithm.hopfencraft.dto.cart.CartResponseDTO;
import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.service.ShoppingCartService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ShoppingCartController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ShoppingCartControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShoppingCartService shoppingCartService;

    private Jwt mockJwt;

    @BeforeEach
    public void setUp() {
        // Create a mock Jwt for testing
        mockJwt = Mockito.mock(Jwt.class);
    }

    @Test
    public void GetCartItems_ValidInput_ReturnsOk() throws Exception {
        // Mock the behavior of your shoppingCartService to return a CartResponseDTO
        List<CartItemDTO> cil = new ArrayList<>();

        CartItemDTO cartItemDTO = new CartItemDTO(1L, new ProductDTO(
                "test", "test", "test", 2, new BigDecimal("2.5")
        ), 1, LocalDateTime.now());

        cil.add(cartItemDTO);
        CartResponseDTO resp = new CartResponseDTO(cil);

        when(shoppingCartService.getCartItems(mockJwt)).thenReturn(ResponseEntity.ok(resp));

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/items")
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestAddToCart_ValidInput_ReturnsOK() throws Exception {
        // Create a CartRegistrationDTO for testing
        CartRegistrationDTO cartRegistrationDTO = new CartRegistrationDTO(1L, 2);

        // Mock the behavior of your shoppingCartService to return a CartResponseDTO
        CartResponseDTO cartResponseDTO = new CartResponseDTO(Collections.emptyList());

        when(shoppingCartService.addToCart(mockJwt, cartRegistrationDTO)).thenReturn(ResponseEntity.ok(cartResponseDTO));

        // Convert the DTO to JSON
        String requestBody = new ObjectMapper().writeValueAsString(cartRegistrationDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //.andExpect(jsonPath("$.brand", equalTo( expectedResponse.getBrand())))

    }

    @Test
    public void TestRemoveCartItem_ValidInput_ReturnsOK() throws Exception {
        // Mock the behavior of your shoppingCartService to return a CartResponseDTO
        Long cartItemId = 1L;
        CartResponseDTO cartResponseDTO = new CartResponseDTO(Collections.emptyList());
        when(shoppingCartService.removeCartItem(mockJwt, cartItemId)).thenReturn(ResponseEntity.ok(cartResponseDTO));

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/remove/{cartItemId}", cartItemId)
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
