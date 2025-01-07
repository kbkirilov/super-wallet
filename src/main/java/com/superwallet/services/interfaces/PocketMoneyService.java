package com.superwallet.services.interfaces;

import com.superwallet.models.PocketMoney;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;

public interface PocketMoneyService {

    void takeFundsFromPocket(PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto);

    void sendFundsToPocket(Wallet walletToWithdraw, PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto);

    PocketMoney getPocketMoneyById (int id);
}

