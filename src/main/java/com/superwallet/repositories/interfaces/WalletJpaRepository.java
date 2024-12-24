package com.superwallet.repositories.interfaces;

import com.superwallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletJpaRepository extends JpaRepository<Wallet, Integer> {


    Optional<Wallet> getWalletByWalletId(int walletId);
}
