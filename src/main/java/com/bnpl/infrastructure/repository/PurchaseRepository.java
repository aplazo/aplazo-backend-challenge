package com.bnpl.infrastructure.repository;

import com.bnpl.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByClientId(Long clientId);
}