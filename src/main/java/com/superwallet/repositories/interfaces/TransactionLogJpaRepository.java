package com.superwallet.repositories.interfaces;

import com.superwallet.models.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogJpaRepository extends JpaRepository<TransactionLog, Integer> {

}
