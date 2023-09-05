package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.CartRegistrationDTO;
import com.hugorithm.hopfencraft.dto.CartResponseDTO;
import com.hugorithm.hopfencraft.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    @Autowired
    public ShoppingCartController( ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/items")
    public ResponseEntity<CartResponseDTO> getCartItems(@AuthenticationPrincipal Jwt jwt) {
        return shoppingCartService.getCartItems(jwt);
    }
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@AuthenticationPrincipal Jwt jwt, @RequestBody CartRegistrationDTO body) {
        return shoppingCartService.addToCart(jwt, body.getProductId(), body.getQuantity());
    }
    @PostMapping("/remove")
    public ResponseEntity<CartResponseDTO> removeCartItem(@AuthenticationPrincipal Jwt jwt, @RequestBody CartRegistrationDTO body) {
        return shoppingCartService.removeCartItem(jwt, body.getCartItemId());
    }

}
