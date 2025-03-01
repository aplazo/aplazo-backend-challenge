package com.bnpl.application;

import com.bnpl.domain.Client;
import com.bnpl.domain.PaymentScheme;
import com.bnpl.domain.Purchase;
import com.bnpl.dto.PurchaseDTO;
import com.bnpl.exceptions.ClientNotFoundException;
import com.bnpl.exceptions.InvalidPurchaseException;
import com.bnpl.exceptions.PurchaseNotFoundException;
import com.bnpl.infrastructure.repository.ClientRepository;
import com.bnpl.infrastructure.repository.PurchaseRepository;
import com.bnpl.mapper.PurchaseMapper;
import com.bnpl.strategy.PaymentSchemeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private PaymentSchemeStrategy paymentSchemeStrategy;

    @InjectMocks
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void registerPurchase_ShouldSavePurchase() {
        Client client = Client.builder()
                .id(1L)
                .name("Hugo")
                .birthDate(LocalDate.of(1992, 4, 10))
                .creditLine(5000.0)
                .build();

        BigDecimal purchaseAmount = BigDecimal.valueOf(1000.0);
        BigDecimal expectedCommission = purchaseAmount.multiply(BigDecimal.valueOf(0.13));
        BigDecimal expectedTotalAmount = purchaseAmount.add(expectedCommission);
        PaymentScheme scheme = PaymentScheme.SCHEME_1;
        PurchaseDTO purchaseDTO = PurchaseDTO.builder()
                .id(1L)
                .clientId(client.getId())
                .purchaseAmount(purchaseAmount)
                .commissionAmount(expectedCommission)
                .totalAmount(expectedTotalAmount)
                .numberOfPayments(5)
                .purchaseDate(LocalDate.now())
                .build();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(paymentSchemeStrategy.assignScheme(client)).thenReturn(scheme);
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
            Purchase p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(purchaseMapper.toDTO(any(Purchase.class))).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.registerPurchase(1L, purchaseAmount);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1000.0, result.getPurchaseAmount().doubleValue());
        assertEquals(130.0, result.getCommissionAmount().doubleValue());
        assertEquals(1130.0, result.getTotalAmount().doubleValue());
        assertEquals(5, result.getNumberOfPayments());

        verify(clientRepository, times(1)).findById(1L);
        verify(paymentSchemeStrategy, times(1)).assignScheme(client);
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
        verify(purchaseMapper, times(1)).toDTO(any(Purchase.class));
    }

    @Test
    void registerPurchaseShouldThrowClientNotFoundException() {
        Long clientId = 99L;
        BigDecimal purchaseAmount = BigDecimal.valueOf(1000.0);

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> purchaseService.registerPurchase(clientId, purchaseAmount));

        verify(clientRepository, times(1)).findById(clientId);
        verifyNoInteractions(paymentSchemeStrategy, purchaseRepository, purchaseMapper);
    }

    @Test
    void registerPurchaseShouldThrowInvalidPurchaseException() {
        Client client = Client.builder()
                .id(1L)
                .name("Carlos")
                .birthDate(LocalDate.of(1995, 1, 1))
                .creditLine(2000.0)
                .build();

        BigDecimal purchaseAmount = BigDecimal.valueOf(3000.0);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        assertThrows(InvalidPurchaseException.class, () -> purchaseService.registerPurchase(1L, purchaseAmount));

        verify(clientRepository, times(1)).findById(1L);
        verifyNoInteractions(paymentSchemeStrategy, purchaseRepository, purchaseMapper);
    }

    @Test
    void getPurchaseByIdShouldReturnPurchase() {
        Long purchaseId = 1L;
        Purchase purchase = Purchase.builder()
                .id(purchaseId)
                .client(new Client())
                .purchaseAmount(BigDecimal.valueOf(1000))
                .commissionAmount(BigDecimal.valueOf(130))
                .totalAmount(BigDecimal.valueOf(1130))
                .numberOfPayments(5)
                .purchaseDate(LocalDate.now())
                .build();

        PurchaseDTO purchaseDTO = PurchaseDTO.builder()
                .id(purchaseId)
                .clientId(1L)
                .purchaseAmount(BigDecimal.valueOf(1000))
                .commissionAmount(BigDecimal.valueOf(130))
                .totalAmount(BigDecimal.valueOf(1130))
                .numberOfPayments(5)
                .purchaseDate(LocalDate.now())
                .build();

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.getPurchaseById(purchaseId);

        assertNotNull(result);
        assertEquals(purchaseId, result.getId());

        verify(purchaseRepository, times(1)).findById(purchaseId);
        verify(purchaseMapper, times(1)).toDTO(purchase);
    }

    @Test
    void getPurchaseByIdShouldThrowPurchaseNotFoundException() {
        Long purchaseId = 99L;
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        assertThrows(PurchaseNotFoundException.class, () -> purchaseService.getPurchaseById(purchaseId));

        verify(purchaseRepository, times(1)).findById(purchaseId);
    }

    @Test
    void getPurchasesByClientShouldReturnListOfPurchases() {
        Long clientId = 1L;
        Purchase purchase1 = Purchase.builder()
                .id(1L)
                .purchaseAmount(BigDecimal.valueOf(1000))
                .commissionAmount(BigDecimal.valueOf(130))
                .totalAmount(BigDecimal.valueOf(1130))
                .numberOfPayments(5)
                .purchaseDate(LocalDate.now())
                .build();

        PurchaseDTO purchaseDTO1 = PurchaseDTO.builder()
                .id(1L)
                .clientId(clientId)
                .purchaseAmount(BigDecimal.valueOf(1000))
                .commissionAmount(BigDecimal.valueOf(130))
                .totalAmount(BigDecimal.valueOf(1130))
                .numberOfPayments(5)
                .purchaseDate(LocalDate.now())
                .build();

        when(purchaseRepository.findByClientId(clientId)).thenReturn(List.of(purchase1));
        when(purchaseMapper.toDTO(purchase1)).thenReturn(purchaseDTO1);

        List<PurchaseDTO> result = purchaseService.getPurchasesByClient(clientId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(purchaseRepository, times(1)).findByClientId(clientId);
        verify(purchaseMapper, times(1)).toDTO(purchase1);
    }
}
