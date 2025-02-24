package com.aplazo.bnpl.challenge.service;

import com.aplazo.bnpl.challenge.application.service.PurchaseServiceImpl;
import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.domain.model.Purchase;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import com.aplazo.bnpl.challenge.infrastructure.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerPurchase_ValidRequest_ShouldCreatePurchase() {
        Client client = new Client(1L, "John Doe", LocalDate.of(1995, 5, 10), 5000);
        PurchaseRequest request = new PurchaseRequest();
        request.setClientId(1L);
        request.setAmount(BigDecimal.valueOf(3000));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PurchaseResponse response = purchaseService.registerPurchase(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Scheme 2", response.getPaymentScheme());
    }

    @Test
    void registerPurchase_ExceedsCredit_ShouldThrowException() {
        Client client = new Client(1L, "John Doe", LocalDate.of(1995, 5, 10), 5000);
        PurchaseRequest request = new PurchaseRequest();
        request.setClientId(1L);
        request.setAmount(BigDecimal.valueOf(6000));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.registerPurchase(request);
        });
    }
}
