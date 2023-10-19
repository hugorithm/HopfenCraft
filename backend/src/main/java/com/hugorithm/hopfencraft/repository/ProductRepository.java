package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);
    Optional<Product> findProductByBrandName(String brandName);
    @Query("select p from Product p where lower(p.brand) like %:keyword% or lower(p.name) like %:keyword%")
    Page<Product> findByProductNameOrDescription(@Param("keyword") String keyword, Pageable pageable);
}
