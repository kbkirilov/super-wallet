package com.superwallet.helpers;

import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoOut;
import com.superwallet.services.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final WalletService walletService;

    @Autowired
    public ModelMapper(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletDtoOut fromWalletTOWalletDtoOut (Wallet wallet) {
        WalletDtoOut dto = new WalletDtoOut();

        dto.setUsername(wallet.getUserId().getUsername());
        dto.setName(wallet.getName());
        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrencyId().getCurrencyCode());
        dto.setStatus(wallet.getStatus().getStatusName());

        return dto;
    }
}
