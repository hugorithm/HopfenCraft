package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, Long> {

}
