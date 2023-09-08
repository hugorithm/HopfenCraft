package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {
    private final ProductRepository productRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public ResponseEntity<ProductDTO> registerProduct(String brand, String name, String description, int quantity, BigDecimal price) {
        try {
            Optional<Product> product = productRepository.findProductByName(name);

            if (product.isPresent()) {
                throw new IllegalStateException("Product already exists");
            }

            Product p = productRepository.save(new Product(brand, name, description, quantity, price));
            return ResponseEntity.ok(new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getRegisterDateTime()
            ));
        } catch (NoSuchElementException | IllegalStateException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }
}
