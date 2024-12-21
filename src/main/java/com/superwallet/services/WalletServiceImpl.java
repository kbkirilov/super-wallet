package com.superwallet.services;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.repositories.interfaces.WalletJpaRepository;
import com.superwallet.services.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.superwallet.helpers.Constants.*;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletJpaRepository walletJpaRepository;

    @Autowired
    public WalletServiceImpl(WalletJpaRepository walletJpaRepository) {
        this.walletJpaRepository = walletJpaRepository;
    }

    @Override
    public Wallet getWalletById(User userAuthenticated, int walletId) {
        checkViewPermissions(userAuthenticated, walletId);

        return walletJpaRepository
                .getWalletByWalletId(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet", walletId));
    }

    private boolean checkIfOwnerOfWallet(User userAuthenticated, int walletId) {
        return userAuthenticated.getWallets()
                .stream()
                .anyMatch(wallet -> wallet.getWalletId() == walletId);
    }

    private void checkViewPermissions (User userAuthenticated, int walletId) {
        if (!(userAuthenticated.getRoleId().getName().equalsIgnoreCase(ADMIN_ROLE) || checkIfOwnerOfWallet(userAuthenticated, walletId))){
            throw new AuthorizationException(CAN_T_SEE_OTHER_USERS_WALLETS_ERROR_MESSAGE);
        }
    }
}
