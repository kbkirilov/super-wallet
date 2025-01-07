package com.superwallet.services.interfaces;

import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;

public interface TransactionLogService {

    void recordDepositTransactionLog(WalletDtoInDepositWithdrawal dto, Wallet wallet);

    void recordWithdrawalTransactionLog(WalletDtoInDepositWithdrawal dto, Wallet wallet);
}
