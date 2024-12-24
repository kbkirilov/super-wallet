package com.superwallet.repositories.interfaces;

import com.superwallet.models.PocketMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PocketMoneyJpaRepository extends JpaRepository<PocketMoney, Integer> {

    Optional<PocketMoney> getPocketMoneyByPocketMoneyId(int id);
}
