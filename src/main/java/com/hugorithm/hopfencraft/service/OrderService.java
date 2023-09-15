package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.cart.CartItemDTO;
import com.hugorithm.hopfencraft.dto.order.OrderResponseDTO;
import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.exception.order.OrderCartIsEmptyException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Order;
import com.hugorithm.hopfencraft.repository.CartItemRepository;
import com.hugorithm.hopfencraft.repository.OrderRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {
    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private List<CartItemDTO> convertCartItemIntoDTO(List<CartItem> cartItems){
        return cartItems.stream()
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
    }

    public ResponseEntity<OrderResponseDTO> createOrder(Jwt jwt) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            List<CartItem> cartItems = user.getCartItems();


            if (cartItems.isEmpty()) {
                throw new OrderCartIsEmptyException("Order failed because shopping cart is empty");
            }

            BigDecimal total = cartItems.stream().map(cartItem -> {
                BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
                return cartItem.getProduct().getPrice().multiply(quantity);
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            Order order = new Order(user, total);
            order.setOrderItems(cartItems);
            order.setOrderStatus(OrderStatus.PENDING);
            //TODO: This is hardcoded for now but will not be later
            order.setCurrency("EUR");

            Order savedOrder = orderRepository.save(order);

            for (CartItem cartItem : cartItems) {
                cartItem.setOrder(savedOrder);
                cartItemRepository.save(cartItem);
            }

            List<CartItemDTO> cartItemsDTO = convertCartItemIntoDTO(cartItems);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new OrderResponseDTO(
                            savedOrder.getOrderId(),
                            savedOrder.getTotal(),
                            cartItemsDTO,
                            savedOrder.getOrderDate()
                    ));
        } catch (UsernameNotFoundException | OrderCartIsEmptyException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }
}
