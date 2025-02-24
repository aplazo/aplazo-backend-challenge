package com.aplazo.bnpl.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private Double amount;

    private int paymentsNumber;

    private String paymentFrequency;

    private int interestRate;

    private Double	commissionAmount;

    private Double installmentAmount;

    private Double totalAmount;

    private LocalDateTime purchaseDate;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
