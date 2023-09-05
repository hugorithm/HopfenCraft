package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public ResponseEntity<ProductRegistrationDTO> registerProduct(String brand, String name, String description, int quantity, BigDecimal price) {
        try {
            Optional<Product> product = productRepository.findProductByName(name);

            if (product.isPresent()) {
                throw new IllegalStateException("Product already exists");
            }

            productRepository.save(new Product(brand, name, description, quantity, price));
            return ResponseEntity.ok(new ProductRegistrationDTO(brand, name, description, quantity, price));
        } catch (NoSuchElementException | IllegalStateException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }

    }
}
