package com.hugorithm.hopfencraft.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hugorithm.hopfencraft.exception.paypal.PaypalAccessTokenException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Transactional
public class PaypalService {
    private final JwtService jwtService;
    private final String clientId;
    private final String clientSecret;
    private final static Logger LOGGER = LoggerFactory.getLogger(PaypalService.class);
    //Needs to be changed with live url for production
    private final String PAYPAL_BASE_URL = "https://api-m.sandbox.paypal.com";

    @Autowired
    public PaypalService(JwtService jwtService,
                         @Value("${paypal.client.id}") String clientId,
                         @Value("${paypal.client.secret}") String clientSecret) {
        this.jwtService = jwtService;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private String getAuth(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private String generateAccessToken() {
        String auth = this.getAuth(this.clientId, this.clientSecret);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(
                PAYPAL_BASE_URL +"/v1/oauth2/token",
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Token generated successfully");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            LOGGER.error("Failed to get Access Token Status code: {}",response.getStatusCode());
            throw new PaypalAccessTokenException("Failed to get Access Token, Status Code: " + response.getStatusCode());
        }
    }

    public ResponseEntity<Object> capturePayment(String orderId) {
        try {
            String accessToken = generateAccessToken();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.set("Authorization", "Bearer " + accessToken);
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(null, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    PAYPAL_BASE_URL + "/v2/checkout/orders/" + orderId + "/capture",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("Paypal order captured");
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            } else {
                LOGGER.error("Failed to capture paypal order");
                return ResponseEntity.status(response.getStatusCode()).body("Failed to capture paypal order");
            }

        } catch (HttpClientErrorException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to capture paypal order");
        } catch (PaypalAccessTokenException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Object> createOrder() {
        try {
            String accessToken = generateAccessToken();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            headers.setContentType(MediaType.APPLICATION_JSON);

            //JSON String
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonNode = objectMapper.createObjectNode();

            jsonNode.put("intent", "CAPTURE");
            ArrayNode purchaseUnitsNode = jsonNode.putArray("purchase_units");
            ObjectNode unitNode = purchaseUnitsNode.addObject();
            ObjectNode amountNode = unitNode.putObject("amount");
            amountNode.put("currency_code", "EUR");
            amountNode.put("value", "100.00");

            String requestJson = jsonNode.toString();

            HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    PAYPAL_BASE_URL + "/v2/checkout/orders",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("Paypal order created");
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            } else {
                LOGGER.error("Failed to create paypal order");
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (PaypalAccessTokenException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
