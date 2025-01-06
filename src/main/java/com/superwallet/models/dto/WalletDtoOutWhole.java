package com.superwallet.models.dto;

import java.math.BigDecimal;

public class WalletDtoOutWhole {

    private String username;
    private String walletName;
    private BigDecimal balance;
    private String currency;
    private String status;
    private Integer depositNotifications;
    private Integer withdrawalNotifications;

    public WalletDtoOutWhole() {
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
