package com.superwallet.helpers;

import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInCreate;
import com.superwallet.models.dto.WalletDtoOut;
import com.superwallet.models.dto.WalletDtoOutBalance;
import com.superwallet.services.interfaces.CurrencyService;
import com.superwallet.services.interfaces.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ModelMapper {

    private final CurrencyService currencyService;
    private final StatusService statusService;

    @Autowired
    public ModelMapper(CurrencyService currencyService, StatusService statusService) {
        this.currencyService = currencyService;
        this.statusService = statusService;
    }

    public WalletDtoOut fromWalletTOWalletDtoOut (Wallet wallet) {
        WalletDtoOut dto = new WalletDtoOut();

        dto.setUsername(wallet.getUser().getUsername());
        dto.setName(wallet.getName());
        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrency().getCurrencyCode());
        dto.setStatus(wallet.getStatus().getStatusName());

        return dto;
    }

    public Wallet fromWalletDtoInCreateToWallet (WalletDtoInCreate walletDtoInCreate, User userAuthenticated) {
        Wallet wallet = new Wallet();

        wallet.setName(walletDtoInCreate.getName());
        wallet.setCurrency(currencyService.getCurrencyByCurrencyCode(walletDtoInCreate.getCurrencyCode()));
        wallet.setUser(userAuthenticated);
        wallet.setStatus(statusService.getStatusById(1));
        wallet.setBalance(BigDecimal.ZERO);

        return wallet;
    }

    public WalletDtoOutBalance fromWalletToWalletDtoOutBalance (Wallet wallet) {
        WalletDtoOutBalance dto = new WalletDtoOutBalance();

        dto.setBalance(wallet.getBalance());

        return dto;
    }
}
