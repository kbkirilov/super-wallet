package com.superwallet.services.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superwallet.models.dto.TransactionNotificationDto;
import com.superwallet.services.MailJetServiceImpl;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.superwallet.helpers.Constants.*;
import static com.superwallet.helpers.Constants.SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_HTMLCONTENT_MESSAGE;

@Service
public class TransactionNotificationConsumer {
    private final MailJetServiceImpl mailJetService;

    public TransactionNotificationConsumer(MailJetServiceImpl mailJetService) {
        this.mailJetService = mailJetService;
    }

    @KafkaListener(topics = "transaction.notifications", groupId = "transaction-notifications")
    public void consumeNotification(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TransactionNotificationDto notificationDto = objectMapper.readValue(message, TransactionNotificationDto.class);

            if (areEmailNotificationsEnabled(notificationDto)) {
                sendTransactionNotificationEmail(notificationDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean areEmailNotificationsEnabled(TransactionNotificationDto notificationDto) {
        if (notificationDto.getTransactionType().equals(TRANSACTION_TYPE_DEPOSIT)) {
            return notificationDto.getDepositNotifications() == 1;
        }
        if (notificationDto.getTransactionType().equals(TRANSACTION_TYPE_WITHDRAWAL)) {
            return notificationDto.getDepositNotifications() == 1;
        }
        return false;
    }

    private void sendTransactionNotificationEmail(TransactionNotificationDto notification) {
        if (notification.getTransactionType().equals(TRANSACTION_TYPE_DEPOSIT)) {
            mailJetService.sendEmail(
                    notification.getEmail(),
                    String.format("%s", notification.getUserFirstName()),
                    String.format(SUCCESSFUL_DEPOSIT_SUBJECT_MESSAGE, notification.getWalletName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE,
                            notification.getUserFirstName(),
                            "deposit",
                            notification.getAmount(),
                            notification.getCurrencyCode(),
                            "to",
                            notification.getWalletName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE,
                            notification.getUserFirstName(),
                            "deposit",
                            notification.getAmount(),
                            notification.getCurrencyCode(),
                            "to",
                            notification.getWalletName())
            );
        }

        if (notification.getTransactionType().equals(TRANSACTION_TYPE_WITHDRAWAL)) {
            mailJetService.sendEmail(
                    notification.getEmail(),
                    String.format("%s", notification.getUserFirstName()),
                    String.format(SUCCESSFUL_WITHDRAWAL_SUBJECT_MESSAGE, notification.getWalletName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_HTMLCONTENT_MESSAGE,
                            notification.getUserFirstName(),
                            "withdrawal",
                            notification.getAmount(),
                            notification.getCurrencyCode(),
                            "from",
                            notification.getWalletName()),
                    String.format(SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE,
                            notification.getUserFirstName(),
                            "withdrawal",
                            notification.getAmount(),
                            notification.getCurrencyCode(),
                            "from",
                            notification.getWalletName())
            );
        }
    }
}
