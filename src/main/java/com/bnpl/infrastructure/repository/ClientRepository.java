package com.bnpl.infrastructure.repository;

import com.bnpl.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
