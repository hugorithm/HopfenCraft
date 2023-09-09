package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.exception.ProductAlreadyExistsException;
import com.hugorithm.hopfencraft.exception.ProductNotFoundException;
import com.hugorithm.hopfencraft.exception.ProductUpdateException;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static io.micrometer.common.util.StringUtils.isNotBlank;

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
                throw new ProductAlreadyExistsException("Product already exists");
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
        } catch (NoSuchElementException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        } catch (ProductAlreadyExistsException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public ResponseEntity<ProductDTO> updateProduct(Long productId, String brand, String name, String description, int quantity, BigDecimal price) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id: %s", productId)));

            if (isNotBlank(brand)) {
                product.setBrand(brand);
            }

            if (isNotBlank(name)) {
                product.setName(name);
            }

            if (isNotBlank(description)) {
                product.setDescription(description);
            }

            if (quantity >= 0) {
                product.setQuantity(quantity);
            }

            if (price != null && price.compareTo(BigDecimal.ZERO) >= 0) {
                product.setPrice(price);
            }

            if (brand.isBlank() && name.isBlank() && description.isBlank() && quantity < 0 && (price == null || price.compareTo(BigDecimal.ZERO) < 0)) {
                throw new ProductUpdateException("At least one field must be updated");
            }

            productRepository.save(product);

            return ResponseEntity.ok(new ProductDTO(
                    product.getProductId(),
                    product.getBrand(),
                    product.getName(),
                    product.getDescription(),
                    product.getQuantity(),
                    product.getPrice(),
                    product.getRegisterDateTime()
            ));

        } catch (ProductNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        } catch (ProductUpdateException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<String> removeProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id: %s", productId)));

            productRepository.delete(product);

            return ResponseEntity.ok("Product removed successfully");
        } catch (ProductNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }
}
