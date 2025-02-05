package com.superwallet.services.kafka;

import com.superwallet.models.User;
import com.superwallet.models.Wallet;
import com.superwallet.models.dto.WalletDtoInDepositWithdrawal;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionActivityProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionActivityProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionActivity(User userAuthenticated, Wallet wallet, WalletDtoInDepositWithdrawal dto, String transactionType) {
        String message = String.format("{\"email\": \"%s\", \"userFirstName\": \"%s\", \"walletName\": \"%s\", \"amount\": \"%f\", \"currencyCode\": \"%s\", \"depositNotifications\": \"%s\", \"withdrawalNotifications\": \"%s\", \"transactionType\": \"%s\", \"transactionTime\": \"%s\"}",
                userAuthenticated.getEmail(),
                userAuthenticated.getFirstName(),
                wallet.getName(),
                dto.getFunds(),
                wallet.getCurrency().getCurrencyCode(),
                wallet.getDepositNotifications(),
                wallet.getWithdrawalNotifications(),
                transactionType,
                LocalDateTime.now());


        kafkaTemplate.send("transaction.notifications", message);
    }
}
