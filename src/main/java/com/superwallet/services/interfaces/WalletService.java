package com.superwallet.services.interfaces;

import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoDepositWithdrawal;
import com.superwallet.models.dto.WalletDtoInUpdate;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    void createWallet(Wallet wallet);

    void checkIfOwnerOfWallet(User userAuthenticated, int walletId);

    Wallet getWalletById(User userAuthenticated, int walletId);

    Wallet updateWallet(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate walletDtoInUpdate);

    Wallet depositToWallet(User userAuthenticated, Wallet walletToDeposit, WalletDtoDepositWithdrawal walletDtoDepositWithdrawal);

    Wallet withdrawalFromWallet(User userAuthenticated, Wallet walletToWithdraw, WalletDtoDepositWithdrawal walletDtoDepositWithdrawal);
}