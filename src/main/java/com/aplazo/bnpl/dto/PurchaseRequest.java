package com.aplazo.bnpl.dto;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long clientId;
    private Double amount;
}
