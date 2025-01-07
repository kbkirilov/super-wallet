package com.superwallet.services;

import com.superwallet.helpers.ModelMapper;
import com.superwallet.models.TransactionLog;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import com.superwallet.repositories.interfaces.TransactionLogJpaRepository;
import com.superwallet.services.interfaces.TransactionLogService;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogServiceImpl implements TransactionLogService {

    private final TransactionLogJpaRepository transactionLogJpaRepository;
    private final ModelMapper modelMapper;

    public TransactionLogServiceImpl(TransactionLogJpaRepository transactionLogJpaRepository, ModelMapper modelMapper) {
        this.transactionLogJpaRepository = transactionLogJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void recordDepositTransactionLog(WalletDtoInDepositWithdrawal dto, Wallet wallet) {
        TransactionLog transactionLog = modelMapper.fromWalletDtoInDepositWithdrawalToTransactionLogDeposit(dto, wallet);

        transactionLogJpaRepository.save(transactionLog);
    }

    @Override
    public void recordWithdrawalTransactionLog(WalletDtoInDepositWithdrawal dto, Wallet wallet) {
        TransactionLog transactionLog = modelMapper.fromWalletDtoInDepositWithdrawalToTransactionLogWithdrawal(dto, wallet);

        transactionLogJpaRepository.save(transactionLog);
    }
}
