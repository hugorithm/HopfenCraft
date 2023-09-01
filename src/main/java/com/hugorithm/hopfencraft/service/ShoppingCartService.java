package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public ShoppingCartService(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<CartItem>> addToCart(ApplicationUser user, Long productId, int quantity) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));

            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);

            return ResponseEntity.ok(user.getCartItems());

        } catch (UsernameNotFoundException | NoSuchElementException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
}
