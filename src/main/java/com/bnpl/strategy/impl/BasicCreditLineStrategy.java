package com.bnpl.strategy.impl;

import com.bnpl.strategy.CreditLineStrategy;
import org.springframework.stereotype.Component;

@Component
public class BasicCreditLineStrategy implements CreditLineStrategy {

    @Override
    public double assignCreditLine(int age) {
        if (age >= 18 && age <= 25) {
            return 3000.0;
        } else if (age >= 26 && age <= 30) {
            return 5000.0;
        } else if (age >= 31 && age <= 65) {
            return 8000.0;
        } else {
            throw new IllegalArgumentException("Clients under 18 or over 65 are not accepted.");
        }
    }
}