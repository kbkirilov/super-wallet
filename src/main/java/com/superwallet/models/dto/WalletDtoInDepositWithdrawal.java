package com.superwallet.models.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class WalletDtoInDepositWithdrawal {

    @DecimalMin(value = "0.01", message = "Balance must be greater than 0.01")
    private BigDecimal funds;

    @Min(value = 1, message = "Pocket money ID must be greater than 0.")
    private int pocketMoneyId;

    @Size(min = 2, max = 500, message = "The payment details message must ming 2 and max 500 characters long!")
    private String paymentDetails;

    public WalletDtoInDepositWithdrawal() {
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

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
