package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.dto.product.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.dto.product.ProductUpdateDTO;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Page<ProductDTO> getProducts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.findAll(pageable);

        return productPage.map(p ->
                new ProductDTO(
                        p.getProductId(),
                        p.getBrand(),
                        p.getName(),
                        p.getDescription(),
                        p.getStockQuantity(),
                        p.getPrice(),
                        p.getCurrency(),
                        p.getRegisterDateTime()
                )
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable @Positive Long productId) {
        Optional<Product> product = productService.findById(productId);

        return product.map(p -> ResponseEntity.ok(
            new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getStockQuantity(),
                    p.getPrice(),
                    p.getCurrency(),
                    p.getRegisterDateTime()
                    )
                )
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/register")
    public ResponseEntity<ProductDTO> registerProduct(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ProductRegistrationDTO body) {
        return productService.registerProduct(jwt, body.getBrand(), body.getName(),body.getDescription(), body.getQuantity(), body.getPrice(), body.getCurrency());
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductUpdateDTO body) {
        return productService.updateProduct(body.getProductId(), body.getBrand(), body.getName(), body.getDescription(), body.getQuantity(), body.getPrice());
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Long productId) {
        return productService.removeProduct(productId);
    }
}
