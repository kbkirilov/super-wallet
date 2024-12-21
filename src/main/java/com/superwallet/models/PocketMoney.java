package com.superwallet.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "pocket_money")
public class PocketMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pocket_money_id")
    private int pocketMoneyId;

    @Column(name = "amount")
    private double amount;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currencyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    public PocketMoney() {
    }

    public int getPocketMoneyId() {
        return pocketMoneyId;
    }

    public void setPocketMoneyId(int pocketMoneyId) {
        this.pocketMoneyId = pocketMoneyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PocketMoney that = (PocketMoney) object;
        return pocketMoneyId == that.pocketMoneyId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pocketMoneyId);
    }
}
