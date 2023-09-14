package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.exception.product.ProductAlreadyExistsException;
import com.hugorithm.hopfencraft.exception.product.ProductNotFoundException;
import com.hugorithm.hopfencraft.exception.product.ProductUpdateException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private final JwtService jwtService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public ResponseEntity<ProductDTO> registerProduct(Jwt jwt, String brand, String name, String description, int quantity, BigDecimal price) {
        try {
            Optional<Product> product = productRepository.findProductByName(name);

            if (product.isPresent()) {
                throw new ProductAlreadyExistsException("Product already exists");
            }
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            Product p = productRepository.save(new Product(brand, name, description, quantity, price, user));
            return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getRegisterDateTime()
            ));
        } catch (NoSuchElementException | UsernameNotFoundException ex) {
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

    public ResponseEntity<ProductDTO> updateProduct(Long productId, String brand, String name, String description, Integer quantity, BigDecimal price) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id: %s", productId)));

            if (brand != null && !brand.isBlank()) {
                product.setBrand(brand);
            }

            if (name != null && !name.isBlank()) {
                product.setName(name);
            }

            if (description != null && !description.isBlank()) {
                product.setDescription(description);
            }

            if (quantity != null) {
                if (quantity >= 0) {
                    product.setQuantity(quantity);
                } else {
                    throw new ProductUpdateException("Quantity must be positive or zero");
                }
            }

            if (price != null) {
                if (price.compareTo(BigDecimal.ZERO) >= 0) {
                    product.setPrice(price);
                } else {
                    throw new ProductUpdateException("Price must be positive or zero");
                }
            }

            // If none of the fields were updated, throw an exception
            if (brand == null && name == null && description == null && quantity == null && price == null) {
                throw new ProductUpdateException("No fields to update");
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
