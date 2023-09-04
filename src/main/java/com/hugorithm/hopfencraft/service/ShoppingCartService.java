package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.CartRegistrationDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ShoppingCartService.class);

    public ShoppingCartService(CartItemRepository cartItemRepository, ProductRepository productRepository, JwtService jwtService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.jwtService = jwtService;
    }

    private  Product getProductFromRepoById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
    }
    public ResponseEntity<CartRegistrationDTO> addToCart(Jwt jwt, Long productId, int quantity) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Product product = getProductFromRepoById(productId);

            List<CartItem> cartItems = user.getCartItems();

            int totalQuantity = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(productId))
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            if (quantity + totalQuantity <= product.getQuantity()) {
                CartItem cartItem = new CartItem(product, user, quantity, LocalDateTime.now());

                cartItemRepository.save(cartItem);
                cartItems.add(cartItem);

                return ResponseEntity.ok(new CartRegistrationDTO(cartItems));
            } else {
                throw new IllegalArgumentException("Requested quantity exceeds available quantity");
            }

        } catch (UsernameNotFoundException | NoSuchElementException | IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<CartRegistrationDTO> removeCartItem(Jwt jwt, Long cartItemId) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            CartItem cartItem = findCartItemById(user, cartItemId);

            if (cartItem != null) {
                user.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);

                return ResponseEntity.ok(new CartRegistrationDTO(user.getCartItems()));
            } else {
                throw new NoSuchElementException("Cart item not found with Id: " + cartItemId);
            }
        } catch (UsernameNotFoundException | NoSuchElementException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private CartItem findCartItemById(ApplicationUser user, Long cartItemId) {
        return user.getCartItems().stream()
                .filter(ci-> ci.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElse(null);
    }
    public ResponseEntity<CartRegistrationDTO> getCartItems(Jwt jwt) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);

            return ResponseEntity.ok(new CartRegistrationDTO(user.getCartItems()));
        } catch (UsernameNotFoundException | NoSuchElementException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
