package com.aplazo.bnpl.entity;

public enum PaymentFrequency {

    WEEKLY(7),
    BIWEEKLY(15),
    MONTHLY(30);
    private final int paymentsNumber;

    PaymentFrequency(int paymentsNumber) {
        this.paymentsNumber = paymentsNumber;
    }

    public int getPaymentsNumber() {
        return paymentsNumber;
    }
}
