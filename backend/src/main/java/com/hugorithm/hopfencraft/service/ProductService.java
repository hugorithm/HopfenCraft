package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import com.hugorithm.hopfencraft.dto.product.ProductRegistrationDTO;
import com.hugorithm.hopfencraft.dto.product.ProductUpdateDTO;
import com.hugorithm.hopfencraft.exception.product.ProductAlreadyExistsException;
import com.hugorithm.hopfencraft.exception.product.ProductImageNotFoundException;
import com.hugorithm.hopfencraft.exception.product.ProductNotFoundException;
import com.hugorithm.hopfencraft.exception.product.ProductUpdateException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.model.ProductImage;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Value("${upload.directory}")
    private String FOLDER_PATH;


    public ResponseEntity<ProductDTO> registerProduct(Jwt jwt, ProductRegistrationDTO dto) {
        try {
            Optional<Product> product = productRepository.findProductByBrandName(dto.getBrand() + dto.getName());

            if (product.isPresent() && product.get().getName().equals(dto.getName()) && product.get().getBrand().equals(dto.getBrand())) {
                throw new ProductAlreadyExistsException("Product already exists");
            }

            ApplicationUser user = jwtService.getUserFromJwt(jwt);

            Product p = productRepository.save(new Product(
                    dto.getBrand(),
                    dto.getName(),
                    dto.getDescription(),
                    dto.getQuantity(),
                    dto.getPrice(),
                    user
            ));

            return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getStockQuantity(),
                    p.getPrice(),
                    Product.getCurrency(),
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

    public ResponseEntity<byte[]> downloadImageFromFileSystem(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product with not found with id: %s", productId));

            ProductImage productImage = product.getImage();

            if (productImage == null) {
                String path = FOLDER_PATH + "/default/beer.png";

                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.IMAGE_PNG)
                        .body(Files.readAllBytes(new File(path).toPath()));
            }

            String path = productImage.getPath();

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(Files.readAllBytes(new File(path).toPath()));
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ProductImageNotFoundException ex) {
            LOGGER.info(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable, String search) {
        try {
            Page<Product> productPage;

            if (search != null && !search.isEmpty()) {
                productPage = productRepository.findByProductNameOrDescription(search, pageable);
            } else {
                productPage = productRepository.findAll(pageable);
            }

            return ResponseEntity.ok(productPage.map(p -> new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getStockQuantity(),
                    p.getPrice(),
                    Product.getCurrency(),
                    p.getRegisterDateTime()
            )));
        } catch (NoSuchElementException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public ResponseEntity<ProductDTO> updateProduct(ProductUpdateDTO dto) {
        try {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: %s", dto.getProductId()));

            if (dto.getBrand() != null && !dto.getBrand().isBlank()) {
                product.setBrand(dto.getBrand());
            }

            if (dto.getName() != null && !dto.getName().isBlank()) {
                product.setName(dto.getName());
            }

            if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
                product.setDescription(dto.getDescription());
            }

            if (dto.getQuantity() != null) {
                if (dto.getQuantity() >= 0) {
                    product.setStockQuantity(dto.getQuantity());
                } else {
                    throw new ProductUpdateException("Quantity must be positive or zero");
                }
            }

            if (dto.getPrice() != null) {
                if (dto.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
                    product.setPrice(dto.getPrice());
                } else {
                    throw new ProductUpdateException("Price must be positive or zero");
                }
            }

            // If none of the fields were updated, throw an exception
            if (dto.getBrand() == null && dto.getName() == null && dto.getDescription() == null && dto.getQuantity() == null && dto.getPrice() == null) {
                throw new ProductUpdateException("No fields to update");
            }

            product.setUpdatedDateTime(LocalDateTime.now());
            Product savedProduct = productRepository.save(product);

            return ResponseEntity.ok(new ProductDTO(
                    savedProduct.getProductId(),
                    savedProduct.getBrand(),
                    savedProduct.getName(),
                    savedProduct.getDescription(),
                    savedProduct.getStockQuantity(),
                    savedProduct.getPrice(),
                    Product.getCurrency(),
                    savedProduct.getRegisterDateTime()
            ));

        } catch (ProductNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        } catch (ProductUpdateException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<String> deleteProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: %s", productId));

            productRepository.delete(product);
        } catch (ProductNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ProductDTO> registerProductImage(Long productId, MultipartFile file) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: %s", productId));

            UUID uuid = UUID.randomUUID();
            String filePath = FOLDER_PATH + uuid + "_" + file.getOriginalFilename();

            ProductImage image = new ProductImage(
                    uuid + "_" + file.getOriginalFilename(),
                    file.getContentType(),
                    filePath
            );

            file.transferTo(new File(filePath));

            product.setImage(image);
            Product p = productRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getStockQuantity(),
                    p.getPrice(),
                    Product.getCurrency(),
                    p.getRegisterDateTime()
            ));
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ProductDTO> updateProductImage(Long productId, MultipartFile file) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: %s", productId));

            File oldFile = new File(product.getImage().getPath());
            if (oldFile.exists()) {
                if (oldFile.delete()) {
                    LOGGER.info("Old file deleted successfully");
                } else {
                    LOGGER.error("Failed to delete old file");
                }
            }

            UUID uuid = UUID.randomUUID();
            String filePath = FOLDER_PATH + uuid + "_" + file.getOriginalFilename();

            ProductImage image = product.getImage();
            image.setName(uuid + "_" + file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setPath(filePath);

            file.transferTo(new File(filePath));
            Product p = productRepository.save(product);

            return ResponseEntity.status(HttpStatus.OK).body(new ProductDTO(
                    p.getProductId(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getStockQuantity(),
                    p.getPrice(),
                    Product.getCurrency(),
                    p.getRegisterDateTime()
            ));
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
