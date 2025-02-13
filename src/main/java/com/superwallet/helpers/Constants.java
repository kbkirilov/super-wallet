package com.superwallet.helpers;

import java.math.BigDecimal;

public class Constants {

    public static final String CAN_T_SEE_OTHER_USERS_WALLETS_ERROR_MESSAGE = "You can't see other users wallets.";
    public static final String CURRENCY_CODE_UPDATE_NON_ZERO_BALANCE_ERROR = "Currency code update is not allowed when the wallet has a non-zero balance.";
    public static final String CURRENT_STATUS_CHANGES_ERROR_MESSAGE = "The current status does not allow you to make any changes to your wallet";
    public static final String YOU_ARE_ALLOWS_TO_USE_ONLY_YOUR_POCKET_MONEY = "You are not allowed to perform action with other users pocket money.";
    public static final String YOU_DON_T_HAVE_ENOUGH_FUNDS_ERROR_MESSAGE = "You don't have enough funds to do the transaction.";
    public static final String THE_CURRENCIES_DOES_NOT_MATCH = "The currencies does not match.";
    public static final String NEGATIVE_TRANSACTION_SUM_ERROR_MESSAGE = "The transaction sum cannot be negative.";
    public static final String LESS_TRANSACTION_SUM_ERROR_MESSAGE = "The transaction sum must be greater than %s";

    public static final String SUCCESSFUL_DEPOSIT_SUBJECT_MESSAGE = "Successful deposit to your %s wallet!";
    public static final String SUCCESSFUL_WITHDRAWAL_SUBJECT_MESSAGE = "Successful withdrawal from your %s wallet!";

    public static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    public static final String TRANSACTION_TYPE_WITHDRAWAL = "WITHDRAWAL";

    public static final String SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_HTMLCONTENT_MESSAGE = """
                <html>
                <body>
                <p>Hello, <strong>%s</strong></p>
                
                <p>We are sending this email to let you know that a <strong>%s</strong> at the value of <strong>%s</strong>%s has been made %s your wallet with name: <strong>%s</strong>.</p>
                
                <p>If you have not made this deposit, please contact us at <a href="mailto:super-wallet@abv.bg"></a>immediately.</p>
                
                <p>Kind Regards,<br/>
                <em>Super-wallet team</em></p>
                </body>
                </html>
                """;
    public static final String SUCCESSFUL_DEPOSIT_AND_WITHDRAWAL_TEXTCONTENT_MESSAGE = """
                Hello, %s
                
                We are sending this email to let you know that a %s at the value of %s%s has been made %s your wallet with name: %s.
                
                If you have not made this deposit, please contact us at super-wallet@abv.bg immediately.
                
                Kind Regards,
                Super-wallet team
                """;

    public static final String FROZEN_STATUS = "Frozen";

    public static final BigDecimal MIN_TRANSACTION_SUM = new BigDecimal("0.01");

}
