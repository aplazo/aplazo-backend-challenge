package com.aplazo.bnpl.controller;


import com.aplazo.bnpl.dto.PurchaseRequest;
import com.aplazo.bnpl.dto.PurchaseResponse;
import com.aplazo.bnpl.entity.Purchase;
import com.aplazo.bnpl.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/register")
    public ResponseEntity<PurchaseResponse> registerPurchase(@RequestBody PurchaseRequest purchaseRequest) {
        Purchase purchase = purchaseService.registerPurchase(
                purchaseRequest.getClientId(),
                purchaseRequest.getAmount()
        );

        PurchaseResponse purchaseResponse = new PurchaseResponse().set(purchase);

        return ResponseEntity.status(201).body(purchaseResponse);
    }
}
