package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.domain.model.Purchase;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import com.aplazo.bnpl.challenge.infrastructure.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final ClientRepository clientRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(ClientRepository clientRepository, PurchaseRepository purchaseRepository) {
        this.clientRepository = clientRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    @Override
    public PurchaseResponse registerPurchase(PurchaseRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (request.getAmount().compareTo(BigDecimal.valueOf(client.getCreditLine())) > 0) {
            throw new IllegalArgumentException("Purchase amount exceeds available credit line");
        }

        String paymentScheme = assignPaymentScheme(client.getName(), client.getId());

        BigDecimal interestRate = paymentScheme.equals("Scheme 1") ? BigDecimal.valueOf(0.13) : BigDecimal.valueOf(0.16);
        BigDecimal totalAmount = request.getAmount().multiply(BigDecimal.ONE.add(interestRate));

        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setAmount(request.getAmount());
        purchase.setPaymentScheme(paymentScheme);
        purchase.setNumberOfPayments(5);
        purchase.setInterestRate(interestRate);
        purchase.setTotalAmount(totalAmount);
        purchase.setPurchaseDate(LocalDate.now());

        purchase = purchaseRepository.save(purchase);

        return new PurchaseResponse(purchase.getId(), purchase.getPaymentScheme(), purchase.getTotalAmount());
    }

    @Override
    public String assignPaymentScheme(String name, Long clientId) {
        if (name.startsWith("C") || name.startsWith("L") || name.startsWith("H")) {
            return "Scheme 1";
        } else if (clientId > 25) {
            return "Scheme 2";
        }

        return "Scheme 2";
    }
}
