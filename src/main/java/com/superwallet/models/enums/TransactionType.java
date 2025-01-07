package com.superwallet.models.enums;

public enum TransactionType {
    DEPOSIT, WITHDRAWAL;

    @Override
    public String toString() {
        switch (this) {
            case DEPOSIT:
                return "DEPOSIT";
            case WITHDRAWAL:
                return "WITHDRAWAL";
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + this);
        }
    }
}
