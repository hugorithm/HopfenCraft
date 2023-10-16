package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.cart.CartItemQuantityUpdateDTO;
import com.hugorithm.hopfencraft.dto.cart.CartRegistrationDTO;
import com.hugorithm.hopfencraft.dto.cart.CartResponseDTO;
import com.hugorithm.hopfencraft.service.ShoppingCartService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/items")
    @RolesAllowed("USER")
    public ResponseEntity<CartResponseDTO> getCartItems(@AuthenticationPrincipal Jwt jwt) {
        return shoppingCartService.getCartItems(jwt);
    }

    @PostMapping("/add")
    @RolesAllowed("USER")
    public ResponseEntity<CartResponseDTO> addToCart(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CartRegistrationDTO body) {
        return shoppingCartService.addToCart(jwt, body);
    }

    @PutMapping("/update")
    @RolesAllowed("USER")
    public ResponseEntity<CartResponseDTO> updateCartItemQuantity(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CartItemQuantityUpdateDTO body) {
        return shoppingCartService.updateCartItemQuantity(jwt, body);
    }

    @DeleteMapping("/remove/{cartItemId}")
    @RolesAllowed("USER")
    public ResponseEntity<CartResponseDTO> removeCartItem(@AuthenticationPrincipal Jwt jwt, @PathVariable @Positive Long cartItemId) {
        return shoppingCartService.removeCartItem(jwt, cartItemId);
    }
}
