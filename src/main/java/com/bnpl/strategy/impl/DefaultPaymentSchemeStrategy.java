package com.bnpl.strategy.impl;

import com.bnpl.domain.Client;
import com.bnpl.domain.PaymentScheme;
import com.bnpl.strategy.PaymentSchemeStrategy;
import org.springframework.stereotype.Component;

@Component
public class DefaultPaymentSchemeStrategy implements PaymentSchemeStrategy {

    @Override
    public PaymentScheme assignScheme(Client client) {
        char firstLetter = Character.toUpperCase(client.getName().charAt(0));

        if (firstLetter == 'C' || firstLetter == 'L' || firstLetter == 'H') {
            return PaymentScheme.SCHEME_1;
        }

        if (client.getId() != null && client.getId() > 25) {
            return PaymentScheme.SCHEME_2;
        }

        return PaymentScheme.SCHEME_2;
    }
}
