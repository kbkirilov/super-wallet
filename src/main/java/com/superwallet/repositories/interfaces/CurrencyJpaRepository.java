package com.superwallet.repositories.interfaces;

import com.superwallet.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyJpaRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> getCurrencyByCurrencyCode(String currencyCode);
}
