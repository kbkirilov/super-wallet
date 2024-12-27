package com.superwallet;

import com.superwallet.models.*;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

public class Helpers {
    public static Currency createMockCurrencyBGN() {
        Currency mockCurrency = new Currency();

        mockCurrency.setCurrencyId(1);
        mockCurrency.setCurrencyCode("BGN");
        mockCurrency.setCurrencyName("Bulgarian Lev");
        mockCurrency.setCountry("Bulgaria");

        return mockCurrency;
    }

    public static Currency createMockCurrencyUSD() {
        Currency mockCurrency = new Currency();

        mockCurrency.setCurrencyId(2);
        mockCurrency.setCurrencyCode("USD");
        mockCurrency.setCurrencyName("United States Dollar");
        mockCurrency.setCountry("United States");

        return mockCurrency;
    }

    public static User createMockUser() {
        User mockUser = new User();

        mockUser.setUserId(1);
        mockUser.setUsername("mockUser");
        mockUser.setPassword("mockPassword");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastName");
        mockUser.setDateOfBirth(LocalDateTime.now());
        mockUser.setAddress("mockAddress");
        mockUser.setPocketMoney(new HashSet<>());
        mockUser.setWallets(new HashSet<>());

        return mockUser;
    }

    public static PocketMoney createMockPocketMoney() {
        PocketMoney mockPocketMoney = new PocketMoney();

        mockPocketMoney.setPocketMoneyId(1);
        mockPocketMoney.setAmount(new BigDecimal(1000));
        mockPocketMoney.setCurrency(createMockCurrencyBGN());
        mockPocketMoney.setUser(createMockUser());

        return mockPocketMoney;
    }

    public static Status createMockStatusActive() {
        Status mockStatus = new Status();

        mockStatus.setStatusId(1);
        mockStatus.setStatusName("Active");

        return mockStatus;
    }

    public static Status createMockStatusFrozen() {
        Status mockStatus = new Status();

        mockStatus.setStatusId(2);
        mockStatus.setStatusName("Frozen");

        return mockStatus;
    }

    public static Wallet createMockWallet() {
        Wallet mockWallet = new Wallet();

        mockWallet.setWalletId(1);
        mockWallet.setName("mockWalletName");
        mockWallet.setCurrency(createMockCurrencyBGN());
        mockWallet.setUser(createMockUser());
        mockWallet.setStatus(createMockStatusActive());
        mockWallet.setBalance(new BigDecimal(1000));

        return mockWallet;
    }

    public static WalletDtoInDepositWithdrawal createMockWalletDtoInDepositWithdrawal() {
        WalletDtoInDepositWithdrawal mockDto = new WalletDtoInDepositWithdrawal();

        mockDto.setPocketMoneyId(1);
        mockDto.setFunds(new BigDecimal("50.00"));

        return mockDto;
    }
}
