package com.superwallet.services;

import com.superwallet.exceptions.EntityNotFoundException;
import com.superwallet.models.PocketMoney;
import com.superwallet.models.dto.WalletDtoDeposit;
import com.superwallet.repositories.interfaces.PocketMoneyJpaRepository;
import com.superwallet.services.interfaces.PocketMoneyService;
import org.springframework.stereotype.Service;

@Service
public class PocketMoneyServiceImpl implements PocketMoneyService {

    private final PocketMoneyJpaRepository pocketMoneyJpaRepository;

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
    public void withdrawFunds(PocketMoney pocketMoney, WalletDtoDeposit dto) {
        pocketMoney.setAmount(pocketMoney.getAmount().subtract(dto.getFunds()));
        pocketMoneyJpaRepository.save(pocketMoney);
    }
}
