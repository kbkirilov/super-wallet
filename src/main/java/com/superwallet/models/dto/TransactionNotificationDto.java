package com.superwallet.models.dto;

import java.math.BigDecimal;

public class TransactionNotificationDto {
    private String email;
    private String userFirstName;
    private String walletName;
    private BigDecimal amount;
    private String currencyCode;
    private int depositNotifications;
    private int withdrawalNotifications;
    private String transactionType;  // "DEPOSIT" or "WITHDRAWAL"
    private String transactionTime;  // e.g., "2025-02-05T14:23:00"

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public int getDepositNotifications() {
        return depositNotifications;
    }

    public void setDepositNotifications(int depositNotifications) {
        this.depositNotifications = depositNotifications;
    }

    public int getWithdrawalNotifications() {
        return withdrawalNotifications;
    }

    public void setWithdrawalNotifications(int withdrawalNotifications) {
        this.withdrawalNotifications = withdrawalNotifications;
    }
}
