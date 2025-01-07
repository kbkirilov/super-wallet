package com.superwallet.services.interfaces;

import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;

import java.math.BigDecimal;

public interface CurrencyExchangeService {
    BigDecimal getConversionRate(String fromCurrency, String toCurrency);

    BigDecimal convertFundsBetweenCurrencies(
            String fromCurrencyCode,
            String toCurrencyCode,
            BigDecimal amount);
}
