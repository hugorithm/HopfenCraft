package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(Pageable pageable, ApplicationUser user);
}
