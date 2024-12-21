package com.superwallet.repositories.interfaces;

import com.superwallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<Wallet, Integer> {


    Optional<Wallet> getWalletByWalletId(int walletId);
}
