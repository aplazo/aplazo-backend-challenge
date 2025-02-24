package com.aplazo.bnpl.service;


import com.aplazo.bnpl.entity.*;
import com.aplazo.bnpl.repository.ClientRepository;
import com.aplazo.bnpl.repository.PurchaseRepository;
import com.aplazo.bnpl.repository.PurchaseRuleRepository;
import com.aplazo.bnpl.repository.UserCreditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PurchaseRuleRepository purchaseRuleRepository;

    @Autowired
    private UserCreditRepository userCreditRepository;

    @Transactional
    public Purchase registerPurchase(Long clientId, Double amount) {

        logger.info("Registering purchase for client ID: {} with amount: {}", clientId, amount);

        Client client;
        try {
            client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        } catch (IllegalArgumentException e) {
            logger.error("Error finding client with ID: {}", clientId, e);
            throw e;
        }

        UserCredit userCredit;
        try {
            userCredit = userCreditRepository.findByUserId(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("User credit not found"));
        } catch (IllegalArgumentException e) {
            logger.error("Error finding user credit for client ID: {}", clientId, e);
            throw e;
        }

        double newCreditUtilized = userCredit.getCreditUtilized() + amount;
        validateCredit(userCredit, newCreditUtilized);

        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setAmount(amount);
        purchase.setPurchaseDate(LocalDateTime.now());

        assignPaymentScheme(purchase, client);

        purchase.setTotalAmount(calculateTotalAmount(purchase));
        purchase.setCommissionAmount(calculateCommissionAmount(purchase));
        purchase.setInstallmentAmount(calculateInstallmentAmount(purchase));

        List<Payment> payments = createPayments(purchase);
        purchase.setPayments(payments);

        userCredit.setCreditUtilized(newCreditUtilized);
        userCreditRepository.save(userCredit);

        logger.info("Registering purchase for client ID: {} with amount: {}", clientId, amount);
        return purchaseRepository.save(purchase);
    }

    private void validateCredit(UserCredit userCredit, Double newCreditUtilized) {
        if (newCreditUtilized > userCredit.getCreditLine()) {
            logger.warn("Credit line exceeded for user ID: {}", userCredit.getUserId());
            throw new IllegalArgumentException("Credit line exceeded");
        }
    }

    private void assignPaymentScheme(Purchase purchase, Client client) {
        List<PurchaseRule> rules = purchaseRuleRepository.findAll();

        if ((client.getName().startsWith("C") || client.getName().startsWith("L") || client.getName().startsWith("H"))) {
            applyRuleToPurchase(purchase, 1L);
        } else if (client.getId() > 25) {
            applyRuleToPurchase(purchase, 2L);
        }else{
            applyRuleToPurchase(purchase, 2L);
        }
    }

    private void applyRuleToPurchase(Purchase purchase, Long purchaseRuleId) {
        PurchaseRule rule;
        try {
            rule = purchaseRuleRepository.findById(purchaseRuleId)
                    .orElseThrow(() -> new IllegalArgumentException("Purchase rule not found"));
        } catch (IllegalArgumentException e) {
            logger.error("Error finding purchase rule with ID: {}", purchaseRuleId, e);
            throw e;
        }

        purchase.setPaymentsNumber(rule.getPaymentsNumber());
        purchase.setPaymentFrequency(rule.getPaymentsFrequency());
        purchase.setInterestRate(rule.getInterestRate());
    }

    private double calculateTotalAmount(Purchase purchase) {
        double interest = purchase.getAmount() * (purchase.getInterestRate() / 100.0);
        return purchase.getAmount() + interest;
    }


    private double calculateCommissionAmount(Purchase purchase) {
        return purchase.getAmount() * (purchase.getInterestRate() / 100.0);
    }


    private double calculateInstallmentAmount(Purchase purchase) {
        return calculateTotalAmount(purchase) / purchase.getPaymentsNumber();
    }


    private List<Payment> createPayments(Purchase purchase) {
        List<Payment> payments = new ArrayList<>();
        LocalDateTime startDate = purchase.getPurchaseDate();
        int intervalDays = PaymentFrequency.valueOf(purchase.getPaymentFrequency()).getPaymentsNumber();

        for (int i = 0; i < purchase.getPaymentsNumber(); i++) {
            Payment payment = new Payment();
            payment.setPurchase(purchase);
            payment.setInstallmentAmount(purchase.getInstallmentAmount());
            payment.setPaymentDate(startDate.plus(i * intervalDays, ChronoUnit.DAYS));
            payment.setStatus(PaymentStatus.PENDING);
            payments.add(payment);
        }

        return payments;
    }

}
