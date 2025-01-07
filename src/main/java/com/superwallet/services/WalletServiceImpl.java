package com.superwallet.services;

import com.superwallet.exceptions.*;
import com.superwallet.models.PocketMoney;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.models.dto.WalletDtoInUpdate;
import com.superwallet.repositories.interfaces.WalletJpaRepository;
import com.superwallet.services.interfaces.*;
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
    private final MailJetServiceImpl mailJetService;
    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    public WalletServiceImpl(WalletJpaRepository walletJpaRepository,
                             CurrencyService currencyService,
                             StatusService statusService,
                             PocketMoneyService pocketMoneyService, MailJetServiceImpl mailJetService, CurrencyExchangeService currencyExchangeService) {

        this.walletJpaRepository = walletJpaRepository;
        this.currencyService = currencyService;
        this.statusService = statusService;
        this.pocketMoneyService = pocketMoneyService;
        this.mailJetService = mailJetService;
        this.currencyExchangeService = currencyExchangeService;
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

        validateWalletUpdate(userAuthenticated, walletToUpdate, dto);

        updateWalletStatus(walletToUpdate, dto);
        updateWalletName(walletToUpdate, dto);
        updateWalletCurrency(walletToUpdate, dto);
        updateDepositNotificationsPreferences(walletToUpdate, dto);
        updateWithdrawalNotificationsPreferences(walletToUpdate, dto);

        walletJpaRepository.save(walletToUpdate);

        return walletToUpdate;
    }

    private void updateDepositNotificationsPreferences(Wallet walletToUpdate, WalletDtoInUpdate dto) {
        Optional.of(dto.getDepositNotifications()).ifPresent(walletToUpdate::setDepositNotifications);
    }

    private void updateWithdrawalNotificationsPreferences(Wallet walletToUpdate, WalletDtoInUpdate dto) {
        Optional.of(dto.getWithdrawalNotifications()).ifPresent(walletToUpdate::setWithdrawalNotifications);
    }

    @Override
    @Transactional
    public Wallet depositToWallet(User userAuthenticated, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto) {
        PocketMoney pocketMoneyOfUser = fetchAndValidatePocketMoney(userAuthenticated, dto, walletToDeposit);

        validateWalletStatus(walletToDeposit);
        validateTransactionAmount(dto);

        processDeposit(pocketMoneyOfUser, walletToDeposit, dto);

        sendDepositNotificationEmail(userAuthenticated, walletToDeposit, dto);

        return walletToDeposit;
    }

    private void sendDepositNotificationEmail(User userAuthenticated, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto) {
        if (walletToDeposit.getDepositNotifications() == 1) {
            mailJetService.sendEmail(
                    userAuthenticated.getEmail(),
                    String.format("%s %s", userAuthenticated.getFirstName(), userAuthenticated.getLastName()),
                    String.format(SUCCESSFUL_DEPOSIT_SUBJECT_MESSAGE, walletToDeposit.getName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE,
                            userAuthenticated.getFirstName(),
                            "deposit",
                            dto.getFunds(),
                            walletToDeposit.getCurrency().getCurrencyCode(),
                            "to",
                            walletToDeposit.getName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE,
                            userAuthenticated.getFirstName(),
                            "deposit",
                            dto.getFunds(),
                            walletToDeposit.getCurrency().getCurrencyCode(),
                            "to",
                            walletToDeposit.getName())
            );
        }
    }

    @Override
    @Transactional
    public Wallet withdrawalFromWallet(User userAuthenticated, Wallet walletToWithdraw, WalletDtoInDepositWithdrawal dto) {
        PocketMoney pocketMoneyOfUser = fetchAndValidatePocketMoney(userAuthenticated, dto, walletToWithdraw);

        validateWalletStatus(walletToWithdraw);
        validateTransactionAmount(dto, walletToWithdraw);

        processWithdrawal(pocketMoneyOfUser, walletToWithdraw, dto);

        sendWithdrawalNotificationEmail(userAuthenticated, walletToWithdraw, dto);

        return walletToWithdraw;
    }

    private void sendWithdrawalNotificationEmail(User userAuthenticated, Wallet walletToWithdraw, WalletDtoInDepositWithdrawal dto) {
        if (walletToWithdraw.getWithdrawalNotifications() == 1) {
            mailJetService.sendEmail(
                    userAuthenticated.getEmail(),
                    String.format("%s %s", userAuthenticated.getFirstName(), userAuthenticated.getLastName()),
                    String.format(SUCCESSFUL_WITHDRAWAL_SUBJECT_MESSAGE, walletToWithdraw.getName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_HTMLCONTENT_MESSAGE,
                            userAuthenticated.getFirstName(),
                            "withdrawal",
                            dto.getFunds(),
                            walletToWithdraw.getCurrency().getCurrencyCode(),
                            "from",
                            walletToWithdraw.getName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE,
                            userAuthenticated.getFirstName(),
                            "withdrawal",
                            dto.getFunds(),
                            walletToWithdraw.getCurrency().getCurrencyCode(),
                            "from",
                            walletToWithdraw.getName())
            );
        }
    }

    @Override
    public void throwIfNotEnoughFundsInWallet(BigDecimal walletFunds, WalletDtoInDepositWithdrawal dto) {
        if (walletFunds.compareTo(dto.getFunds()) < 0) {
            throw new InsufficientFundsException(YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE);
        }
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
    public void throwIfWalletStatusDoesNotAllowUpdates(Wallet wallet) {
        throwIfWalletStatusDoesNotAllowUpdates(wallet, Optional.empty());
    }

    @Override
    public void throwIfWalletStatusDoesNotAllowUpdates(Wallet wallet, Optional<Integer> newStatusId) {
        if (wallet.getStatus().getStatusName().equals(FROZEN_STATUS) && newStatusId.map(id -> id == 1).orElse(false)) {
            return;
        }

        if (wallet.getStatus().getStatusName().equals(FROZEN_STATUS) && newStatusId.isEmpty()) {
            throw new EntityUpdateNotAllowedException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
        }

        if (wallet.getStatus().getStatusName().equals(FROZEN_STATUS)) {
            throw new EntityUpdateNotAllowedException(CURRENT_STATUS_CHANGES_ERROR_MESSAGE);
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

    private PocketMoney fetchAndValidatePocketMoney(User userAuthenticated, WalletDtoInDepositWithdrawal dto, Wallet walletToDeposit) {
        PocketMoney pocketMoneyOfUser = pocketMoneyService.getPocketMoneyById(dto.getPocketMoneyId());
        validatePocketMoneyOwnership(userAuthenticated, dto.getPocketMoneyId());

        return pocketMoneyOfUser;
    }

    private void validatePocketMoneyOwnership(User userAuthenticated, int pocketMoneyId) {
        throwIfNotPocketMoneyOwner(userAuthenticated, pocketMoneyId);
    }

    private void validateWalletStatus(Wallet walletToDeposit) {
        throwIfWalletStatusDoesNotAllowUpdates(walletToDeposit);
    }

    private void validateTransactionAmount(WalletDtoInDepositWithdrawal dto) {
        throwIfTransactionAmountIsNotValid(dto);
    }

    private void validateTransactionAmount(WalletDtoInDepositWithdrawal dto, Wallet wallet) {
        throwIfTransactionAmountIsNotValid(dto);
        throwIfNotEnoughFundsInWallet(wallet.getBalance(), dto);
    }

    private void processDeposit(PocketMoney pocketMoneyOfUser, Wallet walletToDeposit, WalletDtoInDepositWithdrawal dto) {
        pocketMoneyService.takeFundsFromPocket(pocketMoneyOfUser, dto);

        String fromCurrencyCode = pocketMoneyOfUser.getCurrency().getCurrencyCode();
        String toCurrencyCode = walletToDeposit.getCurrency().getCurrencyCode();

        BigDecimal convertedAmount = currencyExchangeService.convertFundsBetweenCurrencies(
                fromCurrencyCode, toCurrencyCode, dto.getFunds());

        walletToDeposit.setBalance(walletToDeposit.getBalance().add(convertedAmount));
        walletJpaRepository.save(walletToDeposit);
    }

    private void processWithdrawal(PocketMoney pocketMoneyOfUser, Wallet walletToWithdraw, WalletDtoInDepositWithdrawal dto) {
        pocketMoneyService.sendFundsToPocket(walletToWithdraw, pocketMoneyOfUser, dto);

        walletToWithdraw.setBalance(walletToWithdraw.getBalance().subtract(dto.getFunds()));
        walletJpaRepository.save(walletToWithdraw);
    }

    private void updateWalletStatus(Wallet walletToUpdate, WalletDtoInUpdate dto) {
        Optional.ofNullable(dto.getStatusId())
                .map(Integer::parseInt)
                .ifPresent(statusId -> walletToUpdate.setStatus(statusService.getStatusById(statusId)));
    }

    private void updateWalletName(Wallet walletToUpdate, WalletDtoInUpdate dto) {
        Optional.ofNullable(dto.getName()).ifPresent(walletToUpdate::setName);
    }

    private void updateWalletCurrency(Wallet walletToUpdate, WalletDtoInUpdate dto) {
        Optional.ofNullable(dto.getCurrencyCode())
                .filter(currencyCode -> !currencyCode.equals(walletToUpdate.getCurrency().getCurrencyCode()))
                .ifPresent(currencyCode -> walletToUpdate.setCurrency(currencyService.getCurrencyByCurrencyCode(currencyCode)));
    }

    private void validateWalletUpdate(User userAuthenticated, Wallet walletToUpdate, WalletDtoInUpdate dto) {
        Optional<Integer> parsedStatusId = Optional.ofNullable(dto.getStatusId()).map(Integer::parseInt);
        throwIfWalletStatusDoesNotAllowUpdates(walletToUpdate, parsedStatusId);

        Optional<String> parsedName = Optional.ofNullable(dto.getName());
        if (parsedName.isPresent()) {
            throwIfWalletNameAlreadyExistsWithinUsersWallets(userAuthenticated, dto.getName(), walletToUpdate);
        }

        Optional<String> parsedCurrencyCode = Optional.ofNullable(dto.getCurrencyCode());
        if (parsedCurrencyCode.isPresent() && !dto.getCurrencyCode().equals(walletToUpdate.getCurrency().getCurrencyCode())) {
            throwIfCurrencyUpdateIsNotAllowed(walletToUpdate);
        }
    }
}
