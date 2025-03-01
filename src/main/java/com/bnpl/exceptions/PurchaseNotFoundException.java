package com.bnpl.exceptions;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(Long id) {
        super("Purchase with ID " + id + " not found.");
    }
}
