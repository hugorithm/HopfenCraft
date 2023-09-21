package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
