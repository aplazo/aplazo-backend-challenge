package com.aplazo.bnpl.dto;

import com.aplazo.bnpl.entity.Purchase;
import lombok.Data;

@Data
public class PurchaseResponse {
    private Long id;

    public PurchaseResponse set(Purchase purchase) {
        this.id = purchase.getId();
        return this;
    }
}
