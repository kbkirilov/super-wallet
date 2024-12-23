package com.superwallet.exceptions;

public class CurrencyUpdateNotAllowedException extends RuntimeException {
    public CurrencyUpdateNotAllowedException(String message) {
        super(message);
    }
}
