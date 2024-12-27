package com.superwallet.services.interfaces;

import com.superwallet.models.PocketMoney;
import com.superwallet.models.User;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;

public interface PocketMoneyService {

    void withdrawFundsFromPocket(PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto);

    void depositFundsToPocket(PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto);

    PocketMoney getPocketMoneyById (int id);
}

