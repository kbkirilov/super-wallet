package com.superwallet.services.interfaces;

import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    Wallet getWalletById(User userAuthenticated, int walletId);

    double getWalletBalance(User userAuthenticated, int walletId);
}
