package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.CartItemDTO;
import com.hugorithm.hopfencraft.dto.CartResponseDTO;
import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ShoppingCartService.class);

    private  Product getProductFromRepoById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
    }
    private ResponseEntity<CartResponseDTO> getCartResponseDTOResponseEntity(List<CartItem> cartItems) {
        List<CartItemDTO> cartItemDTOs = cartItems.stream()
                .map(ci -> new CartItemDTO(
                        ci.getCartItemId(),
                        new ProductDTO(
                                ci.getProduct().getProductId(),
                                ci.getProduct().getBrand(),
                                ci.getProduct().getName(),
                                ci.getProduct().getDescription(),
                                ci.getProduct().getQuantity(),
                                ci.getProduct().getPrice(),
                                ci.getProduct().getRegisterDateTime()
                        ),
                        ci.getQuantity(),
                        ci.getAddedDateTime()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CartResponseDTO(cartItemDTOs));
    }
    public ResponseEntity<CartResponseDTO> addToCart(Jwt jwt, Long productId, int quantity) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Product product = getProductFromRepoById(productId);

            List<CartItem> cartItems = user.getCartItems();

            int totalQuantity = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(productId))
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            if (quantity + totalQuantity <= product.getQuantity()) {
                CartItem cartItem = new CartItem(product, user, quantity);

                cartItemRepository.save(cartItem);
                cartItems.add(cartItem);

                return getCartResponseDTOResponseEntity(cartItems);
            } else {
                throw new IllegalArgumentException("Requested quantity exceeds available quantity");
            }

        } catch (UsernameNotFoundException | NoSuchElementException | IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    public ResponseEntity<CartResponseDTO> removeCartItem(Jwt jwt, Long cartItemId) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            CartItem cartItem = findCartItemById(user, cartItemId);
            List<CartItem> cartItems = user.getCartItems();

            if (cartItem != null) {
                cartItems.remove(cartItem);
                cartItemRepository.delete(cartItem);

                return getCartResponseDTOResponseEntity(cartItems);
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
    public ResponseEntity<CartResponseDTO> getCartItems(Jwt jwt) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            List<CartItem> cartItems = user.getCartItems();

            return getCartResponseDTOResponseEntity(cartItems);
        } catch (UsernameNotFoundException | NoSuchElementException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
