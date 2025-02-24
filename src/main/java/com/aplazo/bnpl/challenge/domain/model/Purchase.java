package com.aplazo.bnpl.challenge.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private BigDecimal amount;
    private String paymentScheme;
    private Integer numberOfPayments;
    private BigDecimal interestRate;
    private BigDecimal totalAmount;
    private LocalDate purchaseDate;

}
