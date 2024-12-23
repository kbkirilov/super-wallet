package com.superwallet.services.interfaces;

import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.models.dto.WalletDtoInUpdate;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    void createWallet(Wallet wallet);

    Wallet getWalletById(User userAuthenticated, int walletId);

    Wallet updateWallet(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate walletDtoInUpdate);

    Wallet depositToWallet(User userAuthenticated, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto);

    Wallet withdrawalFromWallet(User userAuthenticated, Wallet walletToWithdraw, WalletDtoInDepositWithdrawal dto);
}