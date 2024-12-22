package com.superwallet.services.interfaces;

import com.superwallet.models.Currency;

public interface CurrencyService {

    Currency getCurrencyByCurrencyCode(String currencyCode);
}
