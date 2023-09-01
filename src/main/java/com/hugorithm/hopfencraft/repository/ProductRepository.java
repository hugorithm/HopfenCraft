package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findProductByName(String name);
}
