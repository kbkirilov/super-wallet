package com.superwallet.services.interfaces;

import com.superwallet.models.PocketMoney;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.models.dto.WalletDtoInUpdate;

import java.util.Optional;

public interface WalletService {

    void createWallet(Wallet wallet);

    void throwIfNotWalletOwner(User user, int walletId);

    void throwIfNotPocketMoneyOwner(User user, int pocketMoneyId);

    void throwIfCurrenciesDoNotMatch(Wallet wallet, PocketMoney pocketMoney);

    void throwIfCurrencyUpdateIsNotAllowed(Wallet wallet);

    void throwIfWalletStatusDoesNotAllowsUpdates(Wallet wallet);

    void throwIfWalletStatusDoesNotAllowsUpdates(Wallet wallet, Optional<Integer> newStatusId);

    void throwIfTransactionAmountIsNotValid(WalletDtoInDepositWithdrawal dto);

    void throwIfWalletNameAlreadyExistsWithinUsersWallets(User user, String walletNameToUpdate, Wallet walletToUpdate);

    Wallet getWalletById(User userAuthenticated, int walletId);

    Wallet updateWallet(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate walletDtoInUpdate);

    Wallet depositToWallet(User userAuthenticated, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto);

    Wallet withdrawalFromWallet(User userAuthenticated, Wallet walletToWithdraw, WalletDtoInDepositWithdrawal dto);
}