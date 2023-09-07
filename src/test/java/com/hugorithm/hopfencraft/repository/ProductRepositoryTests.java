package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    @Test
    public void ProductRepository_SaveAll_ReturnSavedProduct() {

        //Arrange
        Product product = new Product("Paulaner", "Paulaner", "Weiss", 10, new BigDecimal("2.39"));

        //Act
        Product savedProduct = productRepository.save(product);

        //Assert
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getProductId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepository_GetAll_ReturnMoreThanOneProduct() {
        //Arrange
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"));
        Product product2 = new Product("Franziskaner", "Franziskaner Hefeweizen", "Weiss", 11, new BigDecimal("2.99"));

        //Act
        productRepository.save(product);
        productRepository.save(product2);
        List<Product> userList = productRepository.findAll();

        //Assert
        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void ProductRepository_FindById_ReturnProduct() {
        //Arrange
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"));

        //Act
        productRepository.save(product);
        Product repoProduct = productRepository.findById(product.getProductId()).get();

        //Assert
        Assertions.assertThat(repoProduct).isNotNull();
    }

    @Test
    public void ProductRepository_FindByName_ReturnProductNotNull() {
        //Arrange
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"));

        //Act
        productRepository.save(product);

        Product repoProduct = productRepository.findProductByName(product.getName()).get();

        Assertions.assertThat(repoProduct).isNotNull();
    }

    @Test
    public void ProductRepository_UpdateProduct_ReturnProductNotNull() {
        //Arrange
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"));
        int q = product.getQuantity();
        String b = product.getBrand();
        String n = product.getName();
        String d = product.getDescription();
        BigDecimal pr = product.getPrice();
        LocalDateTime dt = product.getRegisterDateTime();


        //Act
        productRepository.save(product);

        Product repoProduct = productRepository.findById(product.getProductId()).get();
        repoProduct.setBrand("Franziskaner");
        repoProduct.setName("Franziskaner Hefeweizen");
        repoProduct.setQuantity(8);
        repoProduct.setDescription("weizen");
        repoProduct.setPrice(new BigDecimal("1.99"));
        repoProduct.setRegisterDateTime(LocalDateTime.now());

        Product updatedProduct = productRepository.save(repoProduct);

        //Assert
        Assertions.assertThat(updatedProduct.getBrand()).isNotNull();
        Assertions.assertThat(updatedProduct.getName()).isNotNull();
        Assertions.assertThat(updatedProduct.getDescription()).isNotNull();
        Assertions.assertThat(updatedProduct.getPrice()).isNotNull();
        Assertions.assertThat(updatedProduct.getRegisterDateTime()).isNotNull();
        Assertions.assertThat(updatedProduct.getQuantity()).isNotEqualTo(q);
        Assertions.assertThat(updatedProduct.getBrand()).isNotEqualTo(b);
        Assertions.assertThat(updatedProduct.getName()).isNotEqualTo(n);
        Assertions.assertThat(updatedProduct.getDescription()).isNotEqualTo(d);
        Assertions.assertThat(updatedProduct.getPrice()).isNotEqualTo(pr);
        Assertions.assertThat(updatedProduct.getRegisterDateTime()).isNotEqualTo(dt);

    }

    @Test
    public void ProductRepository_ProductDelete_ReturnProductIsEmpty() {
        //Arrange
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"));

        //Act
        productRepository.save(product);

        productRepository.deleteById(product.getProductId());
        Optional<Product> deletedProduct = productRepository.findById(product.getProductId());

        //Assert
        Assertions.assertThat(deletedProduct).isEmpty();
    }
}
