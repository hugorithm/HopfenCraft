package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.order.OrderResponseDTO;
import com.hugorithm.hopfencraft.dto.shippingDetails.ShippingDetailsDTO;
import com.hugorithm.hopfencraft.service.OrderService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/create")
    @RolesAllowed("USER")
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody(required = false) ShippingDetailsDTO shippingDetailsDTO) {
        return orderService.createOrder(jwt, shippingDetailsDTO);
    }

    @GetMapping("/orders/get")
    @RolesAllowed("USER")
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(@AuthenticationPrincipal Jwt jwt,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrders(pageable, jwt);
    }

}
