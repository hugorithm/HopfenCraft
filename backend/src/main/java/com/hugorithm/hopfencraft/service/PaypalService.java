package com.hugorithm.hopfencraft.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hugorithm.hopfencraft.dto.paypal.PaymentRequestDTO;
import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.enums.PaymentMethod;
import com.hugorithm.hopfencraft.exception.order.OrderPaymentException;
import com.hugorithm.hopfencraft.exception.order.OrderNotFoundException;
import com.hugorithm.hopfencraft.exception.order.OrderUserMismatchException;
import com.hugorithm.hopfencraft.exception.paypal.PaypalAccessTokenException;
import com.hugorithm.hopfencraft.model.*;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@Transactional
public class PaypalService {
    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final static Logger LOGGER = LoggerFactory.getLogger(PaypalService.class);
    //TODO: Needs to be changed with live url for production
    @Value("${paypal.base-url}")
    private String PAYPAL_BASE_URL;

    @Autowired
    public PaypalService(JwtService jwtService,
                         OrderRepository orderRepository,
                         OrderService orderService,
                         ShoppingCartService shoppingCartService,
                         @Value("${paypal.client.id}") String clientId,
                         @Value("${paypal.client.secret}") String clientSecret) {
        this.jwtService = jwtService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.CLIENT_ID = clientId;
        this.CLIENT_SECRET = clientSecret;
    }

    private String getAuth(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private String generateAccessToken() {
        try {
            String auth = this.getAuth(CLIENT_ID, CLIENT_SECRET);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Basic " + auth);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
            requestBody.add("grant_type", "client_credentials");

            ResponseEntity<String> response = restTemplate.postForEntity(
                    PAYPAL_BASE_URL + "/v1/oauth2/token",
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                LOGGER.info("Token generated successfully");
                return new JSONObject(response.getBody()).getString("access_token");
            } else {
                LOGGER.error("Failed to get Access Token Status code: {}", response.getStatusCode());
                throw new PaypalAccessTokenException("Failed to get Access Token, Status Code: " + response.getStatusCode());
            }
        } catch (HttpServerErrorException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new OrderPaymentException("Failed to generate paypal oAuth2 token");
        }
    }

    public ResponseEntity<Object> capturePayment(Jwt jwt, PaymentRequestDTO dto, String paypalOrderId) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with id: %s", dto.getOrderId()));

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

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

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

                JsonNode purchaseUnits = responseBody.path("purchase_units");
                if (purchaseUnits.isArray() && !purchaseUnits.isEmpty()) {
                    JsonNode payments = purchaseUnits.get(0).path("payments");
                    if (payments != null) {
                        JsonNode captures = payments.path("captures");
                        if (captures.isArray() && !captures.isEmpty()) {
                            JsonNode transaction = captures.get(0);
                            order.setPaymentTransactionId(transaction.path("id").asText());
                            order.setPaymentTransactionStatus(transaction.path("status").asText());

                            ZonedDateTime utcDateTime = ZonedDateTime.parse(
                                    transaction.path("create_time").asText(),
                                    DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC)
                            );

                            order.setPaymentTransactionDate(utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Lisbon")).toLocalDateTime());
                        }
                    }
                }

                order.setOrderStatus(OrderStatus.PAID);
                Order savedOrder = orderRepository.save(order);

                orderService.updateStock(savedOrder);
                shoppingCartService.clearShoppingCart(user);

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

    public ResponseEntity<Object> createOrder(Jwt jwt, PaymentRequestDTO dto) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with id: %s", dto.getOrderId()));

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
            amountNode.put("currency_code", Order.getCurrency().toString());
            amountNode.put("value", order.getTotal().toString());

            ObjectNode breakdown = amountNode.putObject("breakdown");

            // Create an "item_total" object within the breakdown item
            ObjectNode itemTotal = breakdown.putObject("item_total");

            itemTotal.put("currency_code", Order.getCurrency().toString());
            itemTotal.put("value", order.getTotal().toString());
            ArrayNode items = unitNode.putArray("items");

            for (OrderItem orderItem : order.getOrderItems()) {
                ObjectNode item = items.addObject();
                item.put("name", orderItem.getProduct().getName());
                item.put("description", orderItem.getProduct().getDescription());
                item.put("sku", orderItem.getProduct().getSku());

                ObjectNode unitAmount1 = item.putObject("unit_amount");
                unitAmount1.put("currency_code", Product.getCurrency().toString());
                unitAmount1.put("value", orderItem.getProduct().getPrice().toString());

                item.put("quantity", String.valueOf(orderItem.getQuantity()));
                item.put("category", "PHYSICAL_GOODS");
            }

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
        } catch (HttpServerErrorException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
}
