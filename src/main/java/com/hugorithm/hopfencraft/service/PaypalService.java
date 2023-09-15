package com.hugorithm.hopfencraft.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.enums.PaymentMethod;
import com.hugorithm.hopfencraft.exception.order.OrderPaymentException;
import com.hugorithm.hopfencraft.exception.order.OrderNotFoundException;
import com.hugorithm.hopfencraft.exception.order.OrderUserMismatchException;
import com.hugorithm.hopfencraft.exception.paypal.PaypalAccessTokenException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Order;
import com.hugorithm.hopfencraft.repository.OrderRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private final OrderRepository orderRepository;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final static Logger LOGGER = LoggerFactory.getLogger(PaypalService.class);
    //Needs to be changed with live url for production
    private final static String PAYPAL_BASE_URL = "https://api-m.sandbox.paypal.com";

    @Autowired
    public PaypalService(JwtService jwtService,
                         OrderRepository orderRepository,
                         @Value("${paypal.client.id}") String clientId,
                         @Value("${paypal.client.secret}") String clientSecret) {
        this.jwtService = jwtService;
        this.orderRepository = orderRepository;
        this.CLIENT_ID = clientId;
        this.CLIENT_SECRET = clientSecret;
    }

    private String getAuth(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private String generateAccessToken() {
        String auth = this.getAuth(CLIENT_ID, CLIENT_SECRET);

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

    public ResponseEntity<Object> capturePayment(Jwt jwt, Long orderId, String paypalOrderId) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: %s", orderId));

            if (!order.getUser().getUserId().equals(user.getUserId())) {
                throw new OrderUserMismatchException("Order user id: %s does not match user id: %s", order.getUser().getUserId(), user.getUserId());
            }

            if (order.getOrderStatus().equals(OrderStatus.PAID) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                throw new OrderPaymentException("Order status is PAID or CANCELED");
            }

            String accessToken = generateAccessToken();
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();

            headers.set("Authorization", "Bearer " + accessToken);
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(null, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    PAYPAL_BASE_URL + "/v2/checkout/orders/" + paypalOrderId + "/capture",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("Paypal order captured");

                // Convert response body Object to Json and get id
                ObjectMapper om = new ObjectMapper();
                String responseBodyJson = om.writeValueAsString(response.getBody());
                JsonNode responseBody = om.readTree(responseBodyJson);
                String transactionId = responseBody.get("id").asText();

                order.setPaymentTransactionId(transactionId);
                order.setOrderStatus(OrderStatus.PAID);
                orderRepository.save(order);

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            } else {
                LOGGER.error("Failed to capture paypal order");
                return ResponseEntity.status(response.getStatusCode()).body("Failed to capture paypal order");
            }

        } catch (HttpClientErrorException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to capture paypal order");
        } catch (PaypalAccessTokenException | JsonProcessingException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Object> createOrder(Jwt jwt, Long orderId) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: %s", orderId));

            if (!order.getUser().getUserId().equals(user.getUserId())) {
                throw new OrderUserMismatchException("Order user id: %s does not match user id: %s", order.getUser().getUserId(), user.getUserId());
            }

            if (order.getOrderStatus().equals(OrderStatus.PAID) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                throw new OrderPaymentException("Order status is PAID or CANCELED");
            }

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
            amountNode.put("currency_code", order.getCurrency());
            amountNode.put("value", order.getTotal());
            String requestJson = jsonNode.toString();

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    PAYPAL_BASE_URL + "/v2/checkout/orders",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("Paypal order created");
                order.setOrderStatus(OrderStatus.PROCESSING);
                order.setPaymentMethod(PaymentMethod.PAYPAL);
                orderRepository.save(order);

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            } else {
                LOGGER.error("Failed to create paypal order");
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (PaypalAccessTokenException | OrderUserMismatchException | OrderPaymentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
