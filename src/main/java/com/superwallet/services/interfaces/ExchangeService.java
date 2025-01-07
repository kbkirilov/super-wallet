package com.superwallet.services.interfaces;

import java.math.BigDecimal;

public interface ExchangeService {
    BigDecimal getConversionRate(String fromCurrency, String toCurrency);

    BigDecimal convertFundsBetweenCurrencies(
            String fromCurrencyCode,
            String toCurrencyCode,
            BigDecimal amount);
}
