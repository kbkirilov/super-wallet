package com.superwallet.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private int walletId;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "deposit_notifications")
    private Integer depositNotifications;

    @Column(name = "withdrawal_notifications")
    private Integer withdrawalNotifications;

    @OneToMany(mappedBy = "wallet")
    private List<TransactionLog> transactionLogs;

    public Wallet() {
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public List<TransactionLog> getTransactionLogs() {
        return new ArrayList<>(transactionLogs);
    }

    public void setTransactionLogs(List<TransactionLog> transactionLogHistory) {
        this.transactionLogs = transactionLogHistory;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Wallet wallet = (Wallet) object;
        return walletId == wallet.walletId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(walletId);
    }
}
