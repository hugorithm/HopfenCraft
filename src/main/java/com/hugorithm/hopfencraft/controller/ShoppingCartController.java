package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.CartRegistrationDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import com.hugorithm.hopfencraft.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void getCartItems() {

    }
    @PostMapping("/add")
    //NOT WORKING NEEDS FIX
    public ResponseEntity<List<CartItem>> addToCart(@AuthenticationPrincipal Jwt jwt, @RequestBody CartRegistrationDTO body) {
        String username = jwt.getSubject();
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return shoppingCartService.addToCart(user, body.getProductId(), body.getQuantity());
    }
    @PostMapping("/remove")
    public void removeFromCart(@RequestBody Product product) {

    }

}
