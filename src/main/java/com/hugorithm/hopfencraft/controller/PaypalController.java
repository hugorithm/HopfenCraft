package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.paypal.PaymentRequestDTO;
import com.hugorithm.hopfencraft.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaypalController {
    private final PaypalService paypalService;

    @PostMapping("/api/orders/{orderId}/capture")
    public ResponseEntity<Object> capturePayment(@AuthenticationPrincipal Jwt jwt, @RequestBody PaymentRequestDTO body, @PathVariable("orderId") String paypalOrderId) {
        return paypalService.capturePayment(jwt, body, paypalOrderId);
    }

    @PostMapping("/api/orders/create-order")
    public ResponseEntity<Object> createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody PaymentRequestDTO body) {
        return paypalService.createOrder(jwt, body);
    }

}
