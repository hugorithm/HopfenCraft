package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.CartRegistrationDTO;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import com.hugorithm.hopfencraft.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    private final ShoppingCartService shoppingCartService;
    @Autowired
    public ShoppingCartController(CartItemRepository cartItemRepository, UserRepository userRepository, ShoppingCartService shoppingCartService) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(@AuthenticationPrincipal Jwt jwt) {
        return shoppingCartService.getCartItems(jwt);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal Jwt jwt, @RequestBody CartRegistrationDTO body) {
        return shoppingCartService.addToCart(jwt, body.getProductId(), body.getQuantity());
    }
    @PostMapping("/remove")
    public ResponseEntity<?> removeCartItem(@AuthenticationPrincipal Jwt jwt, @RequestBody CartRegistrationDTO body) {
        return shoppingCartService.removeCartItem(jwt, body.getCartItemId());
    }

}
