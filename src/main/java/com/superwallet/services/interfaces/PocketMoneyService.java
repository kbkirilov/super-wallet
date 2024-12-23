package com.superwallet.services.interfaces;

import com.superwallet.models.PocketMoney;
import com.superwallet.models.dto.WalletDtoDepositWithdrawal;
import org.springframework.stereotype.Service;

@Service
public interface PocketMoneyService {

    PocketMoney getPocketMoneyById (int id);

    void withdrawFundsFromPocket(PocketMoney pocketMoney, WalletDtoDepositWithdrawal dto);

    void depositFundsToPocket(PocketMoney pocketMoney, WalletDtoDepositWithdrawal dto);
}

