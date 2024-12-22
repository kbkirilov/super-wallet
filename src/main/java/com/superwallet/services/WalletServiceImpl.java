package com.superwallet.services;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityDuplicateException;
import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInUpdate;
import com.superwallet.repositories.interfaces.WalletJpaRepository;
import com.superwallet.services.interfaces.CurrencyService;
import com.superwallet.services.interfaces.StatusService;
import com.superwallet.services.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.superwallet.helpers.Constants.*;

@Service
public class WalletServiceImpl implements WalletService {
    
    private final WalletJpaRepository walletJpaRepository;
    private final CurrencyService currencyService;
    private final StatusService statusService;

    @Autowired
    public WalletServiceImpl(WalletJpaRepository walletJpaRepository, CurrencyService currencyService, StatusService statusService) {
        this.walletJpaRepository = walletJpaRepository;
        this.currencyService = currencyService;
        this.statusService = statusService;
    }

    @Override
    public void createWallet(Wallet wallet) {
        walletJpaRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(User userAuthenticated, int walletId) {
        checkIfOwnerOfWallet(userAuthenticated, walletId);

        return walletJpaRepository
                .getWalletByWalletId(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet", walletId));
    }

    @Override
    public Wallet updateWallet(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate dtoInUpdate) {

        int currStatusId = walletToUpdate.getStatus().getStatusId();
        Integer newStatusId = dtoInUpdate.getStatusId() != null ? Integer.parseInt(dtoInUpdate.getStatusId()) : null;

        if (currStatusId == 2 && newStatusId == null) {
            throw new IllegalStateException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
        }

        if (newStatusId != null) {
            if (currStatusId != 1 && newStatusId == 2) {
                throw new IllegalStateException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
            }

            walletToUpdate.setStatus(statusService.getStatusById(newStatusId));
        }

        if (dtoInUpdate.getName() != null) {
            checkIfUserHasWalletWithSameName(userAuthenticated, dtoInUpdate.getName(), walletToUpdate);
            walletToUpdate.setName(dtoInUpdate.getName());
        }

        if (dtoInUpdate.getCurrencyCode() != null) {
            checkIfCurrencyUpdateIsAllowed(walletToUpdate);
            walletToUpdate.setCurrency(currencyService.getCurrencyByCurrencyCode(dtoInUpdate.getCurrencyCode()));
        }

        walletJpaRepository.save(walletToUpdate);

        return walletToUpdate;
    }

    @Override
    public void checkIfOwnerOfWallet(User user, int walletId) {
        boolean isOwner = user.getWallets()
                .stream()
                .anyMatch(wallet -> wallet.getWalletId() == walletId);
        
        if (!isOwner) {
            throw new AuthorizationException(CAN_T_SEE_OTHER_USERS_WALLETS_ERROR_MESSAGE);
        }
    }

    private void checkIfUserHasWalletWithSameName(User user,
                                                  String walletNameToUpdate,
                                                  Wallet walletToUpdate) {
        boolean duplicateNameExists = user.getWallets()
                .stream()
                .anyMatch(wallet -> wallet.getName().equalsIgnoreCase(walletNameToUpdate));

        if (walletToUpdate.getName().equalsIgnoreCase(walletNameToUpdate)) {
            duplicateNameExists = false;
        }

        if (duplicateNameExists) {
            throw new EntityDuplicateException("Wallet", "name", walletNameToUpdate);
        }
    }

    private void checkIfCurrencyUpdateIsAllowed (Wallet wallet) {
        boolean hasMoneyInWallet = wallet.getBalance().compareTo(BigDecimal.ZERO) > 0;

        if (hasMoneyInWallet) {
            throw new IllegalStateException(CURRENCY_CODE_UPDATE_NON_ZERO_BALANCE_ERROR);
        }
    }

}
