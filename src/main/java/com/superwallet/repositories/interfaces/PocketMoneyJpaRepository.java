package com.superwallet.repositories.interfaces;

import com.superwallet.models.PocketMoney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PocketMoneyJpaRepository extends JpaRepository<PocketMoney, Integer> {

    Optional<PocketMoney> getPocketMoneyByPocketMoneyId(int id);
}
