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
import java.util.Optional;

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
        throwIfNotWalletOwner(userAuthenticated, id);

        return walletJpaRepository
                .getWalletByWalletId(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet", id));
    }

    @Override
    public Wallet updateWallet(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate dto) {

        String currStatus = walletToUpdate.getStatus().getStatusName();
        Optional<Integer> parsedStatusId = Optional.ofNullable(dto.getStatusId()).map(Integer::parseInt);
        Optional<String> parsedName = Optional.ofNullable(dto.getName());
        Optional<String> parsedCurrencyCode = Optional.ofNullable(dto.getCurrencyCode());

        throwIfWalletStatusDoesNotAllowsUpdates(walletToUpdate, parsedStatusId);

        parsedStatusId.ifPresent(statusId -> {
            if (!currStatus.equals(ACTIVE_STATUS) && statusId == 2) {
                throw new EntityUpdateNotAllowedException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
            }
            walletToUpdate.setStatus(statusService.getStatusById(statusId));
        });

        if (parsedName.isPresent()) {
            throwIfWalletNameAlreadyExistsWithinUsersWallets(userAuthenticated, dto.getName(), walletToUpdate);
            walletToUpdate.setName(dto.getName());
        }

        if (parsedCurrencyCode.isPresent()) {
            if (!dto.getCurrencyCode().equals(walletToUpdate.getCurrency().getCurrencyCode())) {
                throwIfCurrencyUpdateIsNotAllowed(walletToUpdate);
                walletToUpdate.setCurrency(currencyService.getCurrencyByCurrencyCode(dto.getCurrencyCode()));
            }
        }

        walletJpaRepository.save(walletToUpdate);

        return walletToUpdate;
    }

    @Override
    @Transactional
    public Wallet depositToWallet(User userAuthenticated, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto) {
        PocketMoney pocketMoneyOfUser = pocketMoneyService.getPocketMoneyById(dto.getPocketMoneyId());

        throwIfNotPocketMoneyOwner(userAuthenticated, dto.getPocketMoneyId());
        throwIfCurrenciesDoNotMatch(walletToDeposit, pocketMoneyOfUser);
        throwIfWalletStatusDoesNotAllowsUpdates(walletToDeposit);
        throwIfTransactionAmountIsNotValid(dto);

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
        PocketMoney pocketMoneyOfUser = pocketMoneyService.getPocketMoneyById(dto.getPocketMoneyId());

        throwIfNotPocketMoneyOwner(userAuthenticated, dto.getPocketMoneyId());
        throwIfCurrenciesDoNotMatch(walletToWithdraw, pocketMoneyOfUser);
        throwIfWalletStatusDoesNotAllowsUpdates(walletToWithdraw);
        throwIfTransactionAmountIsNotValid(dto);

        if (walletToWithdraw.getBalance().compareTo(dto.getFunds()) < 0) {
            throw new InsufficientFundsException(YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE);
        }

        walletToWithdraw.setBalance(walletToWithdraw.getBalance().subtract(dto.getFunds()));
        pocketMoneyService.depositFundsToPocket(pocketMoneyOfUser, dto);
        walletJpaRepository.save(walletToWithdraw);

        return walletToWithdraw;
    }

    @Override
    public void throwIfNotWalletOwner(User user, int walletId) {
        if (user.getWallets().stream().noneMatch(wallet -> wallet.getWalletId() == walletId)) {
            throw new AuthorizationException(CAN_T_SEE_OTHER_USERS_WALLETS_ERROR_MESSAGE);
        }
    }

    @Override
    public void throwIfNotPocketMoneyOwner(User user, int pocketMoneyId) {
        if (user.getPocketMoney().stream().noneMatch(pocketMoney -> pocketMoney.getPocketMoneyId() == pocketMoneyId)) {
            throw new AuthorizationException(YOU_ARE_ALLOWS_TO_USE_ONLY_YOUR_POCKET_MONEY);
        }
    }

    @Override
    public void throwIfCurrenciesDoNotMatch(Wallet wallet, PocketMoney pocketMoney) {
        if (!wallet.getCurrency().getCurrencyCode().equals(pocketMoney.getCurrency().getCurrencyCode())) {
            throw new EntityUpdateNotAllowedException(THE_CURRENCIES_DOES_NOT_MATCH);
        }
    }

    @Override
    public void throwIfCurrencyUpdateIsNotAllowed(Wallet wallet) {
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new EntityUpdateNotAllowedException(CURRENCY_CODE_UPDATE_NON_ZERO_BALANCE_ERROR);
        }
    }


    @Override
    public void throwIfWalletStatusDoesNotAllowsUpdates(Wallet wallet) {
        throwIfWalletStatusDoesNotAllowsUpdates(wallet, Optional.empty());
    }

    @Override
    public void throwIfWalletStatusDoesNotAllowsUpdates(Wallet wallet, Optional<Integer> newStatusId) {
        if (wallet.getStatus().getStatusName().equals(FROZEN_STATUS) && newStatusId.isEmpty()) {
            throw new EntityUpdateNotAllowedException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
        }

        if (wallet.getStatus().getStatusName().equals(FROZEN_STATUS)) {
            throw new EntityUpdateNotAllowedException(FROZEN_STATUS_WALLET_ERROR_MESSAGE);
        }
    }

    @Override
    public void throwIfTransactionAmountIsNotValid(WalletDtoInDepositWithdrawal dto) {
        if (dto.getFunds().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionSumException(NEGATIVE_TRANSACTION_SUM_ERROR_MESSAGE);
        }

        if (dto.getFunds().compareTo(MIN_TRANSACTION_SUM) <= 0) {
            throw new InvalidTransactionSumException(String.format(
                    LESS_TRANSACTION_SUM_ERROR_MESSAGE,
                    MIN_TRANSACTION_SUM));
        }
    }

    @Override
    public void throwIfWalletNameAlreadyExistsWithinUsersWallets(User user, String walletNameToUpdate, Wallet walletToUpdate) {
        boolean duplicateNameExists = user.getWallets().stream()
                .anyMatch(wallet -> !wallet.equals(walletToUpdate) && wallet.getName().equalsIgnoreCase(walletNameToUpdate));

        if (duplicateNameExists) {
            throw new EntityDuplicateException("Wallet", "name", walletNameToUpdate);
        }
    }
}
