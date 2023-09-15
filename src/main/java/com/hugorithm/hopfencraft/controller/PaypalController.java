package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.paypal.PaymentRequestDTO;
import com.hugorithm.hopfencraft.service.PaypalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal")
@CrossOrigin("*")
public class PaypalController {
    private final PaypalService paypalService;

    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @PostMapping("/api/orders/{orderId}/capture")
    public ResponseEntity<Object> capturePayment(@AuthenticationPrincipal Jwt jwt, @PathVariable("orderId") String orderId) {
        return paypalService.capturePayment(jwt, orderId);
    }

    @PostMapping("/api/orders/create-order")
    public ResponseEntity<Object> createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody PaymentRequestDTO body) {
        return paypalService.createOrder(jwt, body.getOrderId());
    }

}
