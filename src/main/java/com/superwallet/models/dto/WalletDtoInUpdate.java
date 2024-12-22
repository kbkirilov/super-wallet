package com.superwallet.models.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class WalletDtoInUpdate {

    @Size(min= 2, max=40, message = "Name should be between 2 and 40 symbols!")
    private String name;

    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyCode;

    @Min(value = 1, message = "Status ID must be greater than 0.")
    private String statusId;

    @DecimalMin(value = "0.01", message = "Balance must be greater than 0.01")
    private BigDecimal balance;

    public WalletDtoInUpdate() {
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

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
