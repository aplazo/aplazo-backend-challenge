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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final PurchaseMapper purchaseMapper;
    private final PaymentSchemeStrategy paymentSchemeStrategy;

    public PurchaseDTO registerPurchase(Long clientId, BigDecimal purchaseAmount) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));

        if (purchaseAmount.compareTo(BigDecimal.valueOf(client.getCreditLine())) > 0) {
            throw new InvalidPurchaseException("Purchase amount exceeds the assigned credit line.");
        }

        PaymentScheme scheme = paymentSchemeStrategy.assignScheme(client);
        BigDecimal commission = purchaseAmount.multiply(BigDecimal.valueOf(scheme.getInterestRate()));
        BigDecimal totalAmount = purchaseAmount.add(commission);

        Purchase purchase = Purchase.builder()
                .client(client)
                .purchaseAmount(purchaseAmount)
                .commissionAmount(commission)
                .totalAmount(totalAmount)
                .numberOfPayments(scheme.getNumberOfPayments())
                .purchaseDate(LocalDate.now())
                .build();

        purchaseRepository.save(purchase);
        client.setCreditLine(client.getCreditLine() - totalAmount.doubleValue());
        clientRepository.save(client);

        return purchaseMapper.toDTO(purchase);
    }

    public List<PurchaseDTO> getPurchasesByClient(Long clientId) {
        List<Purchase> purchases = purchaseRepository.findByClientId(clientId);
        return purchases.stream().map(purchaseMapper::toDTO).collect(Collectors.toList());
    }

    public PurchaseDTO getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
        return purchaseMapper.toDTO(purchase);
    }
}

