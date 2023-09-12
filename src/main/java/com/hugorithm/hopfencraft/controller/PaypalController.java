package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.PaymentRequestDTO;
import com.hugorithm.hopfencraft.service.PaypalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal")
@CrossOrigin("*")
public class PaypalController {
    private final PaypalService paypalService;


    private static final String SUCCESS_URL = "http://localhost:8080/paypal/success";
    private static final String CANCEL_URL = "http://localhost:8080/paypal/cancel";

    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paypalService.createPayment(
                        request.getTotal(),
                        request.getCurrency(),
                        request.getMethod(),
                        request.getIntent(),
                        request.getDescription(),
                        SUCCESS_URL,
                        CANCEL_URL
                ));
    }

    @GetMapping(value = "/success")
    public ResponseEntity<String> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        String executeResult = paypalService.executePayment(paymentId, payerId);
        return ResponseEntity.ok(executeResult);
    }
}
