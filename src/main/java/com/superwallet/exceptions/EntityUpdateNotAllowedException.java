package com.superwallet.exceptions;

public class EntityUpdateNotAllowedException extends RuntimeException {
    public EntityUpdateNotAllowedException(String message) {
        super(message);
    }
}
