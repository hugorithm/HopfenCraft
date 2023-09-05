package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
