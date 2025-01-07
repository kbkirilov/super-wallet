package com.superwallet.helpers;

import com.superwallet.models.PocketMoney;
import com.superwallet.models.TransactionLog;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.*;
import com.superwallet.models.enums.TransactionType;
import com.superwallet.services.interfaces.CurrencyService;
import com.superwallet.services.interfaces.PocketMoneyService;
import com.superwallet.services.interfaces.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ModelMapper {

    private final CurrencyService currencyService;
    private final StatusService statusService;
    private final PocketMoneyService pocketMoneyService;

    @Autowired
    public ModelMapper(CurrencyService currencyService, StatusService statusService, PocketMoneyService pocketMoneyService) {
        this.currencyService = currencyService;
        this.statusService = statusService;
        this.pocketMoneyService = pocketMoneyService;
    }

    public WalletDtoOutWhole fromWalletTOWalletDtoOut (Wallet wallet) {
        WalletDtoOutWhole dto = new WalletDtoOutWhole();

        dto.setUsername(wallet.getUser().getUsername());
        dto.setWalletName(wallet.getName());
        dto.setBalance(wallet.getBalance().setScale(2, RoundingMode.HALF_UP));
        dto.setCurrency(wallet.getCurrency().getCurrencyCode());
        dto.setStatus(wallet.getStatus().getStatusName());
        dto.setDepositNotifications(wallet.getDepositNotifications());
        dto.setWithdrawalNotifications(wallet.getWithdrawalNotifications());

        return dto;
    }

    public Wallet fromWalletDtoInCreateToWallet (WalletDtoInCreate walletDtoInCreate, User userAuthenticated) {
        Wallet wallet = new Wallet();

        wallet.setName(walletDtoInCreate.getName());
        wallet.setCurrency(currencyService.getCurrencyByCurrencyCode(walletDtoInCreate.getCurrencyCode()));
        wallet.setUser(userAuthenticated);
        wallet.setStatus(statusService.getStatusById(1));
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setDepositNotifications(0);
        wallet.setWithdrawalNotifications(0);

        return wallet;
    }

    public WalletDtoOutBalance fromWalletToWalletDtoOutBalance (Wallet wallet) {
        WalletDtoOutBalance dto = new WalletDtoOutBalance();

        dto.setBalance(wallet.getBalance());

        return dto;
    }

    public TransactionLog fromWalletDtoInDepositWithdrawalToTransactionLogDeposit(WalletDtoInDepositWithdrawal dto, Wallet wallet) {
        TransactionLog transactionLog = new TransactionLog();
        PocketMoney pocketMoney = pocketMoneyService.getPocketMoneyById(dto.getPocketMoneyId());

        transactionLog.setCreatedAt(LocalDateTime.now());
        transactionLog.setSender(String.format("Pocket money ID:%s", dto.getPocketMoneyId()));
        transactionLog.setReceiver(String.format("Wallet ID:%s", wallet.getWalletId()));
        transactionLog.setPaymentDetails(dto.getPaymentDetails());
        transactionLog.setAmount(String.format("%s %s", dto.getFunds().toPlainString(), pocketMoney.getCurrency().getCurrencyCode()));
        transactionLog.setWalletBalance(String.format("%s %s", wallet.getBalance().setScale(2, RoundingMode.HALF_UP).toPlainString(), wallet.getCurrency().getCurrencyCode()));
        transactionLog.setType(TransactionType.DEPOSIT);
        transactionLog.setWallet(wallet);

        return transactionLog;
    }

    public TransactionLog fromWalletDtoInDepositWithdrawalToTransactionLogWithdrawal(WalletDtoInDepositWithdrawal dto, Wallet wallet) {
        TransactionLog transactionLog = new TransactionLog();

        transactionLog.setCreatedAt(LocalDateTime.now());
        transactionLog.setSender(String.format("Wallet ID:%s", wallet.getWalletId()));
        transactionLog.setReceiver(String.format("Pocket money ID:%s", dto.getPocketMoneyId()));
        transactionLog.setPaymentDetails(dto.getPaymentDetails());
        transactionLog.setAmount(String.format("%s %s", dto.getFunds().toPlainString(), wallet.getCurrency().getCurrencyCode()));
        transactionLog.setWalletBalance(String.format("%s %s", wallet.getBalance().setScale(2, RoundingMode.HALF_UP).toPlainString(), wallet.getCurrency().getCurrencyCode()));
        transactionLog.setType(TransactionType.WITHDRAWAL);
        transactionLog.setWallet(wallet);

        return transactionLog;
    }

    public TransactionLogDtoOut fromTransactionLogToTransactionLogDtoOut(TransactionLog transactionLog) {
        TransactionLogDtoOut dto = new TransactionLogDtoOut();

        dto.setCreateAt(transactionLog.getCreatedAt().toString());
        dto.setSender(transactionLog.getSender());
        dto.setReceiver(transactionLog.getReceiver());
        dto.setPaymentDetails(transactionLog.getPaymentDetails());
        dto.setAmount(transactionLog.getAmount());
        dto.setWalletBalance(transactionLog.getWalletBalance());
        dto.setType(transactionLog.getType().toString());

        return dto;
    }

    public List<TransactionLogDtoOut> fromSetTransactionLogToSetTransactionLogDtoOut(Set<TransactionLog> set) {
        if (set == null) {
            return new ArrayList<>();
        }

        List<TransactionLogDtoOut> dtoOuts = new ArrayList<>();

        for (TransactionLog transactionLog : set) {
            TransactionLogDtoOut dto = fromTransactionLogToTransactionLogDtoOut(transactionLog);
            dtoOuts.add(dto);
        }

        return dtoOuts;

    }
}
