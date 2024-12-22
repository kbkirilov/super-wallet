package com.superwallet.services;

import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.Currency;
import com.superwallet.repositories.interfaces.CurrencyJpaRepository;
import com.superwallet.services.interfaces.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyJpaRepository currencyJpaRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyJpaRepository currencyJpaRepository) {
        this.currencyJpaRepository = currencyJpaRepository;
    }

    @Override
    public Currency getCurrencyByCurrencyCode(String currencyCode) {
        return currencyJpaRepository
                .getCurrencyByCurrencyCode(currencyCode)
                .orElseThrow(() -> new EntityNotFoundException("Currency", "currency code", currencyCode));
    }
}
