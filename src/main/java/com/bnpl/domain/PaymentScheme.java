package com.bnpl.domain;
import lombok.Getter;

import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum PaymentScheme {
    SCHEME_1(5, "Biweekly", 0.13),
    SCHEME_2(5, "Biweekly", 0.16);

    private final int numberOfPayments;
    private final String frequency;
    private final double interestRate;
}
