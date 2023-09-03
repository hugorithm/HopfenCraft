package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product registerProduct(String brand, String name, String description, int quantity, BigDecimal price) {
        Product p = new Product();
        p.setBrand(brand);
        p.setName(name);
        p.setDescription(description);
        p.setQuantity(quantity);
        p.setPrice(price);

        return productRepository.save(p);
    }
}
