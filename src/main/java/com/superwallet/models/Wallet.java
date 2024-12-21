package com.superwallet.models;

import jakarta.persistence.*;

import java.util.Objects;

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
    private Currency currencyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "balance")
    private double balance;

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

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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
