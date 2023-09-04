package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<ProductRegistrationDTO> registerProduct(String brand, String name, String description, int quantity, BigDecimal price) {
        try {
            productRepository.save(new Product(brand, name, description, quantity, price, LocalDateTime.now()));
            return ResponseEntity.ok(new ProductRegistrationDTO(brand, name, description, quantity, price));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }

    }
}
