package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.cart.CartItemDTO;
import com.hugorithm.hopfencraft.dto.order.OrderDTO;
import com.hugorithm.hopfencraft.dto.order.OrderResponseDTO;
import com.hugorithm.hopfencraft.dto.shippingDetails.ShippingDetailsDTO;
import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.exception.order.InsufficientStockException;
import com.hugorithm.hopfencraft.exception.order.OrderCartIsEmptyException;
import com.hugorithm.hopfencraft.model.*;
import com.hugorithm.hopfencraft.repository.OrderRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {
    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartService shoppingCartService;
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public List<OrderDTO> ConvertOrderListIntoOrderDTOList(List<Order> orderList) {

        return orderList.stream()
                .map(o -> new OrderDTO(
                        o.getOrderId(),
                        o.getTotal(),
                        Product.getCurrency(),
                        shoppingCartService.convertCartItemListToCartItemDTOList(o.getUser().getCartItems()),
                        o.getOrderDate()
                ))
                .toList();
    }

    public ResponseEntity<OrderResponseDTO> createOrder(Jwt jwt, ShippingDetailsDTO shippingDetailsDTO) {
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

            Order order = new Order(user, total, OrderStatus.PENDING);

            if (shippingDetailsDTO != null) {
                ShippingDetails shippingDetails = new ShippingDetails(
                        shippingDetailsDTO.getShippingName(),
                        shippingDetailsDTO.getShippingAddress(),
                        shippingDetailsDTO.getShippingCity(),
                        shippingDetailsDTO.getShippingState(),
                        shippingDetailsDTO.getShippingPostalCode(),
                        shippingDetailsDTO.getShippingCountry(),
                        shippingDetailsDTO.getBillingName(),
                        shippingDetailsDTO.getBillingAddress(),
                        shippingDetailsDTO.getBillingCity(),
                        shippingDetailsDTO.getBillingState(),
                        shippingDetailsDTO.getBillingPostalCode(),
                        shippingDetailsDTO.getBillingCountry()
                );

                order.setShippingDetails(shippingDetails);
            }

            Order savedOrder = orderRepository.save(order);

            List<CartItemDTO> cartItemsDTO = shoppingCartService.convertCartItemListToCartItemDTOList(cartItems);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new OrderResponseDTO(
                            savedOrder.getOrderId(),
                            savedOrder.getTotal(),
                            Order.getCurrency().toString(),
                            cartItemsDTO,
                            savedOrder.getOrderStatus(),
                            savedOrder.getOrderDate()
                    ));
        } catch (UsernameNotFoundException | OrderCartIsEmptyException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }

    public void updateStock(Order order) {
        try {
            for (CartItem orderItem : order.getUser().getCartItems()) {
                Product product = orderItem.getProduct();
                int orderedQuantity = orderItem.getQuantity();
                int currentStock = product.getStockQuantity();

                if (currentStock < orderedQuantity) {
                    throw new InsufficientStockException("Insufficient stock for product: %s", product.getProductId());
                }

                // Update stock quantity
                product.setStockQuantity(currentStock - orderedQuantity);
                productRepository.save(product);
            }
        } catch (InsufficientStockException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(Pageable pageable, Jwt jwt) {
        try {
            Page<Order> orderPage = orderRepository.findAll(pageable);
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            List<CartItem> cartItems = user.getCartItems();
            List<CartItemDTO> cartItemsDTO = shoppingCartService.convertCartItemListToCartItemDTOList(cartItems);

            Page<OrderResponseDTO> page = orderPage.map(order -> new OrderResponseDTO(
                    order.getOrderId(),
                    order.getTotal(),
                    Order.getCurrency().toString(),
                    cartItemsDTO,
                    order.getOrderStatus(),
                    order.getOrderDate()
            ));

            return ResponseEntity.ok(page);
        } catch (UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
