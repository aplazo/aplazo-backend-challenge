package com.aplazo.bnpl.controller;

import com.aplazo.bnpl.dto.PurchaseRequest;
import com.aplazo.bnpl.dto.PurchaseResponse;
import com.aplazo.bnpl.entity.Client;
import com.aplazo.bnpl.entity.Purchase;
import com.aplazo.bnpl.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterPurchase() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setClientId(1L);
        purchaseRequest.setAmount(500.0);

        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1900, 1, 1));

        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setClient(client);
        purchase.setAmount(500.0);

        when(purchaseService.registerPurchase(any(Long.class), any(Double.class))).thenReturn(purchase);

        ResponseEntity<PurchaseResponse> response = purchaseController.registerPurchase(purchaseRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }
}