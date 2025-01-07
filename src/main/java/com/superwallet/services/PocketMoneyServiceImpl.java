package com.superwallet.services;

import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.exceptions.InsufficientFundsException;
import com.superwallet.models.PocketMoney;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.repositories.interfaces.PocketMoneyJpaRepository;
import com.superwallet.services.interfaces.CurrencyExchangeService;
import com.superwallet.services.interfaces.PocketMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.superwallet.helpers.Constants.YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE;

@Service
public class PocketMoneyServiceImpl implements PocketMoneyService {

    private final PocketMoneyJpaRepository pocketMoneyJpaRepository;
    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    public PocketMoneyServiceImpl(PocketMoneyJpaRepository pocketMoneyJpaRepository, CurrencyExchangeService currencyExchangeService) {
        this.pocketMoneyJpaRepository = pocketMoneyJpaRepository;
        this.currencyExchangeService = currencyExchangeService;
    }

    @Override
    public PocketMoney getPocketMoneyById(int id) {
        return pocketMoneyJpaRepository
                .getPocketMoneyByPocketMoneyId(id)
                .orElseThrow(() -> new EntityNotFoundException("PocketMoney", id));
    }

    @Override
    public void takeFundsFromPocket(PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto) {
        throwIfNotEnoughFundsInPocketMoney(pocketMoney.getAmount(), dto);

        pocketMoney.setAmount(pocketMoney.getAmount().subtract(dto.getFunds()));
        pocketMoneyJpaRepository.save(pocketMoney);
    }

    @Override
    public void sendFundsToPocket(Wallet walletToWithdraw, PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto) {
        String fromCurrencyCode = walletToWithdraw.getCurrency().getCurrencyCode();
        String toCurrencyCode = pocketMoney.getCurrency().getCurrencyCode();

        BigDecimal convertedAmount = currencyExchangeService.convertFundsBetweenCurrencies(
                fromCurrencyCode, toCurrencyCode, dto.getFunds());

        pocketMoney.setAmount(pocketMoney.getAmount().add(convertedAmount));
        pocketMoneyJpaRepository.save(pocketMoney);
    }

    private static void throwIfNotEnoughFundsInPocketMoney(BigDecimal pocketMoneyFunds, WalletDtoInDepositWithdrawal dto) {
        if (pocketMoneyFunds.compareTo(dto.getFunds()) < 0) {
            throw new InsufficientFundsException(YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE);
        }
    }
}
