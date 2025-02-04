package com.superwallet.services;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityDuplicateException;
import com.superwallet.exceptions.EntityUpdateNotAllowedException;
import com.superwallet.exceptions.InvalidTransactionSumException;
import com.superwallet.models.*;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.repositories.interfaces.WalletJpaRepository;
import com.superwallet.services.interfaces.PocketMoneyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import  static com.superwallet.Helpers.*;
import static com.superwallet.helpers.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTests {

    @Mock
    private WalletJpaRepository mockWalletJpaRepository;

    @Mock
    private PocketMoneyService pocketMoneyService;

    @Mock
    private WalletJpaRepository walletJpaRepository;

    @InjectMocks
    private WalletServiceImpl mockWalletService;

    @Test
    public void getWalletById_ShouldThrowException_When_UserIsNotOwner() {
        Wallet mockWallet = createMockWallet();
        User mockUser = createMockUser();
        User otherUser = createMockUser();

        mockWallet.setUser(otherUser);
        otherUser.setWallets(new HashSet<>(Collections.singleton(mockWallet)));
        mockUser.setWallets(new HashSet<>());

        assertThrows(AuthorizationException.class,
                () -> mockWalletService.getWalletById(mockUser, 1));
    }

    @Test
    public void throwIfNotWalletOwner_ShouldThrow_When_UserIsNotNotWalletOwner() {
        Wallet mockWallet = createMockWallet();
        User mockUser = createMockUser();
        Wallet anotherMockWallet = createMockWallet();
        anotherMockWallet.setWalletId(2);

        mockUser.setWallets(Collections.singleton(anotherMockWallet));

        assertThrows(AuthorizationException.class,
                () -> mockWalletService.throwIfNotWalletOwner(mockUser, mockWallet.getWalletId()));

    }

    @Test
    public void throwIfNotWalletOwner_ShouldNotThrow_When_UserIsNotWalletOwner() {
        Wallet mockWallet = createMockWallet();
        User mockUser = createMockUser();
        mockUser.setWallets(Collections.singleton(mockWallet));

        mockWalletService.throwIfNotWalletOwner(mockUser, 1);

    }

    @Test
    public void throwIfNotPocketMoneyOwner_ShouldThrow_When_UserIsNotNotPocketMoneyOwner() {
        PocketMoney mockPocketMoney = createMockPocketMoney();
        User mockUser = createMockUser();
        PocketMoney anotherPocketMoney = createMockPocketMoney();
        anotherPocketMoney.setPocketMoneyId(2);

        mockUser.setPocketMoney(Collections.singleton(anotherPocketMoney));

        assertThrows(AuthorizationException.class,
                () -> mockWalletService.throwIfNotWalletOwner(mockUser, mockPocketMoney.getPocketMoneyId()));

    }

    @Test
    public void throwIfNotPocketMoneyOwner_ShouldNotThrow_When_UserIsPocketMoneyOwner() {
        PocketMoney mockPocketMoney = createMockPocketMoney();
        User mockUser = createMockUser();
        mockUser.setPocketMoney(Collections.singleton(mockPocketMoney));

        mockWalletService.throwIfNotPocketMoneyOwner(mockUser, mockPocketMoney.getPocketMoneyId());
    }

    @Test
    public void throwIfCurrenciesDoNotMatch_ShouldThrow_When_CurrenciesDoNotMatch() {
        Wallet mockWallet = createMockWallet();
        PocketMoney mockPocketMoney = createMockPocketMoney();
        Currency mockCurrencyUsd = createMockCurrencyUSD();

        mockPocketMoney.setCurrency(mockCurrencyUsd);

        assertThrows(EntityUpdateNotAllowedException.class,
                () -> mockWalletService.throwIfCurrenciesDoNotMatch(mockWallet, mockPocketMoney));

    }

    @Test
    public void throwIfCurrenciesDoNotMatch_ShouldNotThrow_When_CurrenciesMatch() {
        Wallet mockWallet = createMockWallet();
        PocketMoney mockPocketMoney = createMockPocketMoney();

        mockWalletService.throwIfCurrenciesDoNotMatch(mockWallet, mockPocketMoney);
    }

    @Test
    public void throwIfCurrencyUpdateIsNotAllowed_ShouldThrow_When_WalletBalanceAboveZero() {
        Wallet mockWallet = createMockWallet();

        assertThrows(EntityUpdateNotAllowedException.class,
                () -> mockWalletService.throwIfCurrencyUpdateIsNotAllowed(mockWallet));
    }

    @Test
    public void throwIfCurrencyUpdateIsNotAllowed_ShouldNotThrow_When_WalletBalanceIsZero() {
        Wallet mockWallet = createMockWallet();
        mockWallet.setBalance(BigDecimal.ZERO);

        mockWalletService.throwIfCurrencyUpdateIsNotAllowed(mockWallet);
    }

    @Test
    public void throwIfWalletStatusDoesNotAllowsUpdates_ShouldThrow_When_WalletStatusIsFrozen() {
        Wallet mockWallet = createMockWallet();
        Status mockStatusFrozen = createMockStatusFrozen();
        mockWallet.setStatus(mockStatusFrozen);

        assertThrows(EntityUpdateNotAllowedException.class,
                () -> mockWalletService.throwIfWalletStatusDoesNotAllowUpdates(mockWallet));
    }

    @Test
    public void throwIfWalletStatusDoesNotAllowsUpdates_ShouldNotThrow_When_WalletStatusIsActive() {
        Wallet mockWallet = createMockWallet();

        mockWalletService.throwIfWalletStatusDoesNotAllowUpdates(mockWallet);
    }

    @Test
    public void throwIfWalletStatusDoesNotAllowsUpdates_ShouldThrow_When_WalletStatusIsFrozenAndNewStatusIdIsEmpty() {
        Wallet mockWallet = createMockWallet();
        Status mockStatusFrozen = createMockStatusFrozen();
        mockWallet.setStatus(mockStatusFrozen);
        Optional<Integer> mockNewStatusId = Optional.empty();

        assertThrows(EntityUpdateNotAllowedException.class,
                () -> mockWalletService.throwIfWalletStatusDoesNotAllowUpdates(mockWallet, mockNewStatusId));
    }

    @Test
    public void throwIfWalletStatusDoesNotAllowsUpdates_ShouldThrow_When_WalletStatusIsFrozenAndNewStatusIdIsFrozen() {
        Wallet mockWallet = createMockWallet();
        Status mockStatusFrozen = createMockStatusFrozen();
        mockWallet.setStatus(mockStatusFrozen);
        Optional<Integer> mockNewStatusId = Optional.of(2);

        assertThrows(EntityUpdateNotAllowedException.class,
                () -> mockWalletService.throwIfWalletStatusDoesNotAllowUpdates(mockWallet, mockNewStatusId));
    }

    @Test
    public void throwIfWalletStatusDoesNotAllowsUpdates_ShouldNotThrow_When_WalletStatusIsActiveAndNewStatusIdIsPresent() {
        Wallet mockWallet = createMockWallet();
        Optional<Integer> mockNewStatusId = Optional.of(1);

        mockWalletService.throwIfWalletStatusDoesNotAllowUpdates(mockWallet, mockNewStatusId);
    }

    @Test
    public void throwIfTransactionAmountIsNotValid_ShouldThrow_When_AmountIsNegative() {
        WalletDtoInDepositWithdrawal mockDto = Mockito.mock(WalletDtoInDepositWithdrawal.class);
        Mockito.when(mockDto.getFunds()).thenReturn(new BigDecimal("-1.00"));

        InvalidTransactionSumException exception = assertThrows(InvalidTransactionSumException.class,
                () -> mockWalletService.throwIfTransactionAmountIsNotValid(mockDto));

        assertEquals(NEGATIVE_TRANSACTION_SUM_ERROR_MESSAGE, exception.getMessage());

    }

    @Test
    public void throwIfTransactionAmountIsNotValid_ShouldThrow_When_AmountIsLessThanMinTransactionAmount() {
        WalletDtoInDepositWithdrawal mockDto = Mockito.mock(WalletDtoInDepositWithdrawal.class);
        Mockito.when(mockDto.getFunds()).thenReturn(new BigDecimal("0.00"));

        InvalidTransactionSumException exception = assertThrows(InvalidTransactionSumException.class,
                () -> mockWalletService.throwIfTransactionAmountIsNotValid(mockDto));

        assertEquals(String.format(LESS_TRANSACTION_SUM_ERROR_MESSAGE, MIN_TRANSACTION_SUM), exception.getMessage());

    }

    @Test
    public void throwIfTransactionAmountIsNotValid_ShouldNotThrow_When_AmountIsValid() {
        WalletDtoInDepositWithdrawal mockDto = Mockito.mock(WalletDtoInDepositWithdrawal.class);
        Mockito.when(mockDto.getFunds()).thenReturn(new BigDecimal("20.00"));

        assertDoesNotThrow(() -> mockWalletService.throwIfTransactionAmountIsNotValid(mockDto));

    }

    @Test
    public void throwIfWalletNameAlreadyExistsWithinUsersWallets_ShouldThrow_When_WalletNameAlreadyPresent() {
        Wallet mockWallet = createMockWallet();

        Wallet mockWalletToUpdate = createMockWallet();
        mockWalletToUpdate.setName("mockName");
        mockWalletToUpdate.setWalletId(2);

        User mockUser = createMockUser();

        mockUser.setWallets(Set.of(mockWallet, mockWalletToUpdate));

        assertThrows(EntityDuplicateException.class,
                () -> mockWalletService.throwIfWalletNameAlreadyExistsWithinUsersWallets(
                        mockUser, mockWallet.getName(), mockWalletToUpdate));


    }

    @Test
    public void throwIfWalletNameAlreadyExistsWithinUsersWallets_ShouldNotThrow_When_WalletNameNotPresent() {
        Wallet mockWallet = createMockWallet();
        User mockUser = createMockUser();

        mockUser.setWallets(Collections.singleton(mockWallet));

        assertDoesNotThrow(() -> mockWalletService.throwIfWalletNameAlreadyExistsWithinUsersWallets(
                        mockUser, "mockName", mockWallet));
    }
}





























