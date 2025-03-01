package com.bnpl.strategy;

import com.bnpl.domain.Client;
import com.bnpl.domain.PaymentScheme;

public interface PaymentSchemeStrategy {
    PaymentScheme assignScheme(Client client);
}
