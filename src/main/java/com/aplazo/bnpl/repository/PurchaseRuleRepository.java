package com.aplazo.bnpl.repository;

import com.aplazo.bnpl.entity.PurchaseRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRuleRepository extends JpaRepository<PurchaseRule, Long> {
}
