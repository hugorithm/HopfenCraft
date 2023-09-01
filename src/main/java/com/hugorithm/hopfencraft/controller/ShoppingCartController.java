package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private List<Product> cart = new ArrayList<>();

    @GetMapping("/items")
    public List<Product> getCartItems() {
        return cart;
    }
    @PostMapping("/add")
    public List<Product> addToCart(@RequestBody Product product) {
        cart.add(product);
        return cart;
    }
    @PostMapping("/remove")
    public List<Product> removeFromCart(@RequestBody Product product) {
        cart.removeIf(item -> item.getProductId() == product.getProductId());
        return cart;
    }

}
