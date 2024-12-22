package com.superwallet.exceptions;

public class EntityDuplicateException extends RuntimeException {
  public EntityDuplicateException(String message) {
    super(message);
  }

  public EntityDuplicateException(String type, int id) {
    this(type, "id", String.valueOf(id));
  }

  public EntityDuplicateException(String type, String attribute, String value) {
    super(String.format("%s with %s %s already exists.", type, attribute, value));
  }
}
