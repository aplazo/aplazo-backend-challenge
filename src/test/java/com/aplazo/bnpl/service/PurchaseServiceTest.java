package com.aplazo.bnpl.service;

import com.aplazo.bnpl.entity.Client;
import com.aplazo.bnpl.entity.Purchase;
import com.aplazo.bnpl.entity.PurchaseRule;
import com.aplazo.bnpl.entity.UserCredit;
import com.aplazo.bnpl.repository.ClientRepository;
import com.aplazo.bnpl.repository.PurchaseRepository;
import com.aplazo.bnpl.repository.PurchaseRuleRepository;
import com.aplazo.bnpl.repository.UserCreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserCreditRepository userCreditRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    @Mock
    private PurchaseRuleRepository purchaseRuleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterPurchase() {
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1900, 1, 1));

        UserCredit userCredit = new UserCredit();
        userCredit.setId(1L);
        userCredit.setUserId(1L);
        userCredit.setCreditLine(1000.0);
        userCredit.setCreditUtilized(0.0);

        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setPaymentFrequency("BIWEEKLY");
        purchase.setClient(client);
        purchase.setAmount(500.0);

        PurchaseRule purchaseRule = new PurchaseRule();
        purchaseRule.setId(1L);
        purchaseRule.setRuleName("Test Rule");
        purchaseRule.setPaymentsNumber(5);
        purchaseRule.setPaymentsFrequency("BIWEEKLY");
        purchaseRule.setInterestRate(5);

        PurchaseRule purchaseRule2 = new PurchaseRule();
        purchaseRule2.setId(2L);
        purchaseRule2.setRuleName("Test Rule 2");
        purchaseRule2.setPaymentsNumber(5);
        purchaseRule2.setPaymentsFrequency("BIWEEKLY");
        purchaseRule2.setInterestRate(8);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        when(userCreditRepository.findByUserId(any())).thenReturn(Optional.of(userCredit));

        when(purchaseRuleRepository.findAll()).thenReturn(Arrays.asList(purchaseRule, purchaseRule2));

        when(purchaseRuleRepository.findById(2L)).thenReturn(Optional.of(purchaseRule2));

        Purchase registeredPurchase = purchaseService.registerPurchase(1L, 500.0);

        assertEquals(1L, registeredPurchase.getId());
        assertEquals(1L, registeredPurchase.getClient().getId());
        assertEquals(500.0, registeredPurchase.getAmount());
    }

    @Test
    public void testRegisterPurchaseClientNotFound() {
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.registerPurchase(1L, 500.0);
        });

        assertEquals("Client not found", exception.getMessage());
    }



    @Test
    public void testRegisterPurchaseCreditExceeded() {
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        UserCredit userCredit = new UserCredit();
        userCredit.setUserId(1L);
        userCredit.setCreditLine(1000.0);
        userCredit.setCreditUtilized(900.0);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(client));
        when(userCreditRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(userCredit));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.registerPurchase(1L, 200.0);
        });

        assertEquals("Credit line exceeded", exception.getMessage());
    }



}