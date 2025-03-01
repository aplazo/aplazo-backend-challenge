package com.bnpl.infrastructure.controller;

import com.bnpl.application.PurchaseService;
import com.bnpl.dto.PurchaseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/register")
    public ResponseEntity<PurchaseDTO> registerPurchase(@RequestParam Long clientId,
                                                        @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(purchaseService.registerPurchase(clientId, amount));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<PurchaseDTO>> getPurchasesByClient(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchasesByClient(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }
}
