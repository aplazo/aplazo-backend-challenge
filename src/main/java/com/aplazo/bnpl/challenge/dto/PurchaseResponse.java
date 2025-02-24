package com.aplazo.bnpl.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

    private Long id;

    private String paymentScheme;

    private BigDecimal totalAmount;

}
