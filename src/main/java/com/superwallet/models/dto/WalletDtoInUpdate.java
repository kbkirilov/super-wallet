package com.superwallet.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WalletDtoInUpdate {

    @Size(min= 2, max=40, message = "Name should be between 2 and 40 symbols!")
    private String name;

    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyCode;

    @Min(value = 1, message = "Status ID must be greater than 0.")
    private String statusId;

    private Integer depositNotifications;

    private Integer withdrawalNotifications;

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

    public Integer getDepositNotifications() {
        return depositNotifications;
    }

    public void setDepositNotifications(Integer depositNotifications) {
        this.depositNotifications = depositNotifications;
    }

    public Integer getWithdrawalNotifications() {
        return withdrawalNotifications;
    }

    public void setWithdrawalNotifications(Integer withdrawalNotifications) {
        this.withdrawalNotifications = withdrawalNotifications;
    }
}
