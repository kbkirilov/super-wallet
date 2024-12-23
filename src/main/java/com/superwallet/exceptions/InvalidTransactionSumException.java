package com.superwallet.exceptions;

public class InvalidTransactionSumException extends RuntimeException {
    public InvalidTransactionSumException(String message) {
        super(message);
    }
}
