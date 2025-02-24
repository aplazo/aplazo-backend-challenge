package com.aplazo.bnpl.repository;

import com.aplazo.bnpl.entity.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCreditRepository  extends JpaRepository<UserCredit, Long> {
    Optional<UserCredit> findByUserId(Long userId);
}
