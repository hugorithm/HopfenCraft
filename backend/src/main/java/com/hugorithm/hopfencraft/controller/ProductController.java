package com.hugorithm.hopfencraft.controller;

import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.dto.product.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.dto.product.ProductUpdateDTO;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.service.ProductService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "15") int size,
                                                        @RequestParam(name = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("productId")));
        return productService.getProducts(pageable, search);
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
                                p.getSku(),
                                p.getPrice(),
                                Product.getCurrency(),
                                p.getRegisterDateTime()
                        )
                )
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/register")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ProductDTO> registerProduct(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ProductRegistrationDTO body) {
        return productService.registerProduct(jwt, body);
    }

    @PostMapping("{productId}/image/register")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ProductDTO> registerProductImage(@PathVariable Long productId, MultipartFile file) {
        return productService.registerProductImage(productId, file);
    }

    @PutMapping("{productId}/image/update")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, MultipartFile file) {
        return productService.updateProductImage(productId, file);
    }

    @PutMapping("/update")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductUpdateDTO body) {
        return productService.updateProduct(body);
    }

    @DeleteMapping("/delete/{productId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("{productId}/image")
    @PermitAll
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long productId) {
        return  productService.downloadImageFromFileSystem(productId);
    }
}
