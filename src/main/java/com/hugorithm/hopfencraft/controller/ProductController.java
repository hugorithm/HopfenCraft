package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.ProductDTO;
import com.hugorithm.hopfencraft.dto.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
                        p.getQuantity(),
                        p.getPrice(),
                        p.getRegisterDateTime()
                )
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.findById(productId);

        return product.map(p -> ResponseEntity.ok(
            new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getRegisterDateTime()
                    )
                )
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<ProductDTO> registerProduct(@Validated @RequestBody ProductRegistrationDTO body) {
        return productService.registerProduct(body.getBrand(), body.getName(),body.getDescription(), body.getQuantity(), body.getPrice());
    }
}
