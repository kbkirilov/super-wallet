package com.superwallet.services;

import com.superwallet.exceptions.*;
import com.superwallet.models.PocketMoney;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.models.dto.WalletDtoInUpdate;
import com.superwallet.repositories.interfaces.WalletJpaRepository;
import com.superwallet.services.interfaces.CurrencyService;
import com.superwallet.services.interfaces.PocketMoneyService;
import com.superwallet.services.interfaces.StatusService;
import com.superwallet.services.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.superwallet.helpers.Constants.*;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletJpaRepository walletJpaRepository;
    private final CurrencyService currencyService;
    private final StatusService statusService;
    private final PocketMoneyService pocketMoneyService;

    @Autowired
    public WalletServiceImpl(WalletJpaRepository walletJpaRepository,
                             CurrencyService currencyService,
                             StatusService statusService,
                             PocketMoneyService pocketMoneyService) {

        this.walletJpaRepository = walletJpaRepository;
        this.currencyService = currencyService;
        this.statusService = statusService;
        this.pocketMoneyService = pocketMoneyService;
    }

    @Override
    public void createWallet(Wallet wallet) {
        walletJpaRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(User userAuthenticated, int id) {
        checkIfOwnerOfWallet(userAuthenticated, id);

        return walletJpaRepository
                .getWalletByWalletId(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet", id));
    }

    @Override
    public Wallet updateWallet(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate dto) {

        int currStatusId = walletToUpdate.getStatus().getStatusId();
        Integer newStatusId = dto.getStatusId() != null ? Integer.parseInt(dto.getStatusId()) : null;

        if (currStatusId == 2 && newStatusId == null) {
            throw new EntityUpdateNotAllowedException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
        }

        if (newStatusId != null) {
            if (currStatusId != 1 && newStatusId == 2) {
                throw new EntityUpdateNotAllowedException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
            }

            walletToUpdate.setStatus(statusService.getStatusById(newStatusId));
        }

        if (dto.getName() != null) {
            checkIfUserHasWalletWithSameName(userAuthenticated, dto.getName(), walletToUpdate);
            walletToUpdate.setName(dto.getName());
        }

        if (dto.getCurrencyCode() != null) {
            if (!dto.getCurrencyCode().equals(walletToUpdate.getCurrency().getCurrencyCode())) {
                checkIfCurrencyUpdateIsAllowed(walletToUpdate);
                walletToUpdate.setCurrency(currencyService.getCurrencyByCurrencyCode(dto.getCurrencyCode()));
            }
        }

        walletJpaRepository.save(walletToUpdate);

        return walletToUpdate;
    }

    @Override
    @Transactional
    public Wallet depositToWallet(User userAuthenticated, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto) {
        checkIfUserIsOwnerOfPocketMoney(userAuthenticated, dto.getPocketMoneyId());
        PocketMoney pocketMoneyOfUser = pocketMoneyService.getPocketMoneyById(dto.getPocketMoneyId());
        checkCurrenciesMatch(walletToDeposit, pocketMoneyOfUser);

        checkIfWalletStatusAllowsUpdated(walletToDeposit);

        if (pocketMoneyOfUser.getAmount().compareTo(dto.getFunds()) < 0) {
            throw new InsufficientFundsException(YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE);
        }

        pocketMoneyService.withdrawFundsFromPocket(pocketMoneyOfUser, dto);
        walletToDeposit.setBalance(walletToDeposit.getBalance().add(dto.getFunds()));
        walletJpaRepository.save(walletToDeposit);

        return walletToDeposit;
    }

    @Override
    @Transactional
    public Wallet withdrawalFromWallet(User userAuthenticated, Wallet walletToWithdraw, WalletDtoInDepositWithdrawal dto) {
        checkIfUserIsOwnerOfPocketMoney(userAuthenticated, dto.getPocketMoneyId());
        PocketMoney pocketMoneyOfUser = pocketMoneyService.getPocketMoneyById(dto.getPocketMoneyId());
        checkCurrenciesMatch(walletToWithdraw, pocketMoneyOfUser);

        checkIfWalletStatusAllowsUpdated(walletToWithdraw);

        if (walletToWithdraw.getBalance().compareTo(dto.getFunds()) < 0) {
            throw new InsufficientFundsException(YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE);
        }

        walletToWithdraw.setBalance(walletToWithdraw.getBalance().subtract(dto.getFunds()));
        pocketMoneyService.depositFundsToPocket(pocketMoneyOfUser, dto);
        walletJpaRepository.save(walletToWithdraw);

        return walletToWithdraw;
    }

    @Override
    public void checkIfOwnerOfWallet(User user, int id) {
        boolean isOwner = user.getWallets()
                .stream()
                .anyMatch(wallet -> wallet.getWalletId() == id);
        
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
            throw new EntityUpdateNotAllowedException(CURRENCY_CODE_UPDATE_NON_ZERO_BALANCE_ERROR);
        }
    }

    private void checkIfUserIsOwnerOfPocketMoney (User user, int id) {
        boolean isOwner = user.getPocketMoney()
                .stream()
                .anyMatch(pocketMoney -> pocketMoney.getPocketMoneyId() == id);

        if (!isOwner) {
            throw new AuthorizationException(YOU_ARE_ALLOWS_TO_USE_ONLY_YOUR_POCKET_MONEY);
        }
    }

    private void checkCurrenciesMatch(Wallet wallet, PocketMoney pocketMoney) {
        String walletCurrencyCode = wallet.getCurrency().getCurrencyCode();
        String pocketMoneyCurrencyCode = pocketMoney.getCurrency().getCurrencyCode();

        if (!walletCurrencyCode.equalsIgnoreCase(pocketMoneyCurrencyCode)) {
            throw new EntityUpdateNotAllowedException(THE_CURRENCIES_DOES_NOT_MATCH);
        }
    }

    private void checkIfWalletStatusAllowsUpdated(Wallet walletToUpdate) {
        if (walletToUpdate.getStatus().getStatusName().equals(FROZEN_STATUS)) {
            throw new EntityDuplicateException(FROZEN_STATUS_WALLET_ERROR_MESSAGE);
        }
    }

}
