package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.OrderDTO;
import com.hugorithm.hopfencraft.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
    private final OrderService orderService;
    @Autowired

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal Jwt jwt){
        return orderService.createOrder(jwt);
    }
}
