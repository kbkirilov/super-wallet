package com.superwallet.services.interfaces;

import com.superwallet.models.PocketMoney;
import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoDeposit;
import org.springframework.stereotype.Service;

@Service
public interface PocketMoneyService {

    PocketMoney getPocketMoneyById (int id);

    void withdrawFunds (PocketMoney pocketMoney, WalletDtoDeposit dto);
}
