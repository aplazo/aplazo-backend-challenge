package com.bnpl.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDTO {

    private Long id;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Purchase amount is required")
    @DecimalMin(value = "0.01", message = "Purchase amount must be greater than zero")
    private BigDecimal purchaseAmount;

    private BigDecimal commissionAmount;

    private BigDecimal totalAmount;

    private Integer numberOfPayments;

    private LocalDate purchaseDate;
}
