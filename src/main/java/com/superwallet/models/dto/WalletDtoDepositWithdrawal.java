package com.superwallet.models.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class WalletDtoDepositWithdrawal {

    @DecimalMin(value = "0.01", message = "Balance must be greater than 0.01")
    private BigDecimal funds;

    @Min(value = 1, message = "Pocket money ID must be greater than 0.")
    private int pocketMoneyId;

    public WalletDtoDepositWithdrawal() {
    }

    public BigDecimal getFunds() {
        return funds;
    }

    public void setFunds(BigDecimal funds) {
        this.funds = funds;
    }

    public int getPocketMoneyId() {
        return pocketMoneyId;
    }

    public void setPocketMoneyId(int pocketMoneyId) {
        this.pocketMoneyId = pocketMoneyId;
    }
}
