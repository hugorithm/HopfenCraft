package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.cart.CartItemDTO;
import com.hugorithm.hopfencraft.dto.cart.CartRegistrationDTO;
import com.hugorithm.hopfencraft.dto.cart.CartResponseDTO;
import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.exception.cart.CartItemNotFoundException;
import com.hugorithm.hopfencraft.exception.cart.CartItemQuantityExceedsAvailableException;
import com.hugorithm.hopfencraft.exception.product.ProductNotFoundException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ShoppingCartService.class);

    private Product getProductFromRepoById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    public List<CartItemDTO> convertCartItemListToCartItemDTOList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(ci -> new CartItemDTO(
                        ci.getCartItemId(),
                        new ProductDTO(
                                ci.getProduct().getProductId(),
                                ci.getProduct().getBrand(),
                                ci.getProduct().getName(),
                                ci.getProduct().getDescription(),
                                ci.getProduct().getStockQuantity(),
                                ci.getProduct().getPrice(),
                                Product.getCurrency(),
                                ci.getProduct().getRegisterDateTime()
                        ),
                        ci.getQuantity(),
                        ci.getTotal(),
                        ci.getAddedDateTime()
                ))
                .toList();
    }

    public ResponseEntity<CartResponseDTO> addToCart(Jwt jwt, CartRegistrationDTO dto) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Product product = getProductFromRepoById(dto.getProductId());

            List<CartItem> cartItems = user.getCartItems();

            int totalQuantity = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(dto.getProductId()))
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            Optional<CartItem> hasProduct = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(product.getProductId()))
                    .findFirst();

            if (dto.getQuantity() + totalQuantity <= product.getStockQuantity()) {

                BigDecimal total =  product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));

                if (hasProduct.isPresent()) {
                    CartItem cartItem = hasProduct.get();
                    int sum = cartItem.getQuantity() + dto.getQuantity();
                    cartItem.setQuantity(sum);
                    cartItemRepository.save(cartItem);
                } else {
                    CartItem cartItem = new CartItem(product, user, dto.getQuantity(), total);
                    cartItemRepository.save(cartItem);
                    cartItems.add(cartItem);
                }

                return ResponseEntity.ok(new CartResponseDTO(convertCartItemListToCartItemDTOList(cartItems)));
            } else {
                throw new CartItemQuantityExceedsAvailableException("Requested quantity exceeds available quantity");
            }

        } catch (UsernameNotFoundException | ProductNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CartItemQuantityExceedsAvailableException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

                return ResponseEntity.ok(new CartResponseDTO(convertCartItemListToCartItemDTOList(cartItems)));
            } else {
                throw new CartItemNotFoundException("Cart item not found with Id: " + cartItemId);
            }
        } catch (UsernameNotFoundException | CartItemNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private CartItem findCartItemById(ApplicationUser user, Long cartItemId) {
        return user.getCartItems().stream()
                .filter(ci -> ci.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElse(null);
    }

    public ResponseEntity<CartResponseDTO> getCartItems(Jwt jwt) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            List<CartItem> cartItems = user.getCartItems();

            return ResponseEntity.ok(new CartResponseDTO(convertCartItemListToCartItemDTOList(cartItems)));
        } catch (UsernameNotFoundException | NoSuchElementException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public void clearShoppingCart(ApplicationUser user) {
        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);
        user.getCartItems().clear();
        cartItemRepository.deleteAll(cartItems);
        userRepository.save(user);
    }

}
