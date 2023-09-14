package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.service.PaypalService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> capturePayment(@PathVariable("orderId") String orderId) {
        return paypalService.capturePayment(orderId);
    }

    @PostMapping("/api/orders/create-order")
    public ResponseEntity<Object> createOrder() {
        return paypalService.createOrder();
    }

}
