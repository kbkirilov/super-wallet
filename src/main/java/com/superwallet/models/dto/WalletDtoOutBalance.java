package com.superwallet.models.dto;

import java.math.BigDecimal;
import java.util.PrimitiveIterator;

public class WalletDtoOutBalance {

    private BigDecimal balance;

    public WalletDtoOutBalance() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
