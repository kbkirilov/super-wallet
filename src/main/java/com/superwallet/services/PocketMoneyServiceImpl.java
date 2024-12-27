package com.superwallet.services;

import com.superwallet.exceptions.AuthorizationException;
import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.PocketMoney;
import com.superwallet.models.User;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.repositories.interfaces.PocketMoneyJpaRepository;
import com.superwallet.services.interfaces.PocketMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.superwallet.helpers.Constants.YOU_ARE_ALLOWS_TO_USE_ONLY_YOUR_POCKET_MONEY;

@Service
public class PocketMoneyServiceImpl implements PocketMoneyService {

    private final PocketMoneyJpaRepository pocketMoneyJpaRepository;

    @Autowired
    public PocketMoneyServiceImpl(PocketMoneyJpaRepository pocketMoneyJpaRepository) {
        this.pocketMoneyJpaRepository = pocketMoneyJpaRepository;
    }

    @Override
    public PocketMoney getPocketMoneyById(int id) {
        return pocketMoneyJpaRepository
                .getPocketMoneyByPocketMoneyId(id)
                .orElseThrow(() -> new EntityNotFoundException("PocketMoney", id));
    }

    @Override
    public void withdrawFundsFromPocket(PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto) {
        pocketMoney.setAmount(pocketMoney.getAmount().subtract(dto.getFunds()));
        pocketMoneyJpaRepository.save(pocketMoney);
    }

    @Override
    public void depositFundsToPocket(PocketMoney pocketMoney, WalletDtoInDepositWithdrawal dto) {
        pocketMoney.setAmount(pocketMoney.getAmount().add(dto.getFunds()));
        pocketMoneyJpaRepository.save(pocketMoney);
    }
}
