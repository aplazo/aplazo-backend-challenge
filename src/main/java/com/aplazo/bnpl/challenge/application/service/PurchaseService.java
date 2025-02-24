package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;

/**
 * Service responsible for managing purchases.
 */
public interface PurchaseService {

    PurchaseResponse registerPurchase(PurchaseRequest request);

    String assignPaymentScheme(String name, Long clientId);

}
