package com.superwallet.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WalletDtoInCreate {

    @NotNull(message = "Name cannot be null!")
    @Size(min= 2, max=40, message = "Name should be between 2 and 40 symbols!")
    private String name;

    @NotNull(message = "Currency code cannot be null!")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyCode;

    public WalletDtoInCreate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
