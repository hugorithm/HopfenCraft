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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ShoppingCartService(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private ApplicationUser getUserFromJwt(Jwt jwt) {
        String username = jwt.getSubject();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private  Product getProductFromRepoById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
    }
    public ResponseEntity<?> addToCart(Jwt jwt, Long productId, int quantity) {
        try {
            ApplicationUser user = getUserFromJwt(jwt);
            Product product = getProductFromRepoById(productId);

            /*
            int totalQuantity = user.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(productId))
                    .mapToInt(CartItem::getQuantity)
                    .sum();
            */

            if (quantity <= product.getQuantity()) {
                CartItem cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);

                cartItemRepository.save(cartItem);

                return ResponseEntity.ok(user.getCartItems());
            } else {
                throw new IllegalArgumentException("Requested quantity exceeds available quantity");
            }

        } catch (UsernameNotFoundException | NoSuchElementException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    public ResponseEntity<?> removeCartItem(Jwt jwt, Long cartItemId) {
        try {
            ApplicationUser user = getUserFromJwt(jwt);
            CartItem cartItem = findCartItemById(user, cartItemId);
            if (cartItem != null) {
                user.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);

                return ResponseEntity.ok(user.getCartItems());
            } else {
                throw new NoSuchElementException("Cart item not found with Id: " + cartItemId);
            }
        } catch (UsernameNotFoundException | NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    private CartItem findCartItemById(ApplicationUser user, Long cartItemId) {
        return user.getCartItems().stream()
                .filter(ci-> ci.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElse(null);
    }
    public ResponseEntity<?> getCartItems(Jwt jwt) {
        try {
            ApplicationUser user = getUserFromJwt(jwt);

            return ResponseEntity.ok(user.getCartItems());
        } catch (UsernameNotFoundException | NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
