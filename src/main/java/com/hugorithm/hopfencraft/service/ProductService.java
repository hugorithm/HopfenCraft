package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product registerProduct(String brand, String name, String description, int quantity, BigDecimal price) {
        return productRepository.save(new Product(brand, name, description, quantity, price, LocalDateTime.now()));
    }
}
