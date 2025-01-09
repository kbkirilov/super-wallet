# super-wallet

## 💡 Description

SupperWallet is a simple API that allows users to interact with a virtual `Wallet`. It is built according to the layered architecture style, where we are layers for the controllers, services, and repositories. <br>

Each `User` can have multiple wallets as his possession. 

Each `Wallet` can have a `name`, `balance`, `currencyCode` (ISO 4217), and `status`. 
Users can `deposit()`/`withdraw()` money to/from their wallets. These are some of the operations the user can make with a wallet:

- `createWallet()`
  - A user can't have two wallets with the same name, but two wallets with the same name can exist in the database.
  - Each wallet can be created with a given currency. At the moment the database supports the currencies of the 6
    strongest economies.
- `updateWallet()` 
  - The user can only update his own wallet's information.
  - The user can change the `status` on their wallet to either *Active* or *Frozen*. A wallet with status set to *Frozen* can't
  make deposits and withdrawals.
  - The user can update the `depositNotifications` and `withdrawalNotifications` fields if he wants to receive email notifications when making deposits or withdrawals.
  - The wallet's `currencyCode` can be changed only if its balance is equal to 0.
- `getWalletBalance()`
  - A user can only get the balance of their own wallets.
- `getWalletById()` - Gets all the information about the wallet.
- `depositToWallet()`
  - Takes money from the user's `PocketMoney` and transfers it to his wallet of choice.
  - If `depositNotifications` is set to 1 the user receives an email notification every time he deposits money from his pocket to his wallet.
  - If the wallet status is `Frozen` no deposits can be made to the wallet.
  - If the deposit transaction amount is less than `0.01` the deposit can't be made.
  - If the pocket money currency code is different from the receiving wallet currency code the system will fetch the latest exchange rate and convert the funds automatically.
  - If there are no funds in the pocket money the deposit can't be made.
  - Once the deposit is processed a deposit `TransactionLog` is recorded. 
- `withdrawFromWallet()`
  - Takes money from the user's wallet and transfers it to their `PocketMoney` of choice.
  - If `withdrawalNotifications` is set to 1 the user receives an email notification every time he withdraws money from his wallet to his pocket money.
  - If the wallet status is `Frozen` no withdrawals can be made from the wallet.
  - If the withdrawal transaction amount is less than `0.01` the withdrawal can't be made.
  - If the wallet currency code is different from the receiving pocket money currency code the system will fetch the latest exchange rate and convert the funds automatically.
  - If there are no funds in the wallet the withdrawal can't be made.
  - Once the withdrawal is processed a withdrawal `TransactionLog` is recorded.
- `getWalletTransactionLogHistory()`
  - It gets a list of all transaction logs for the wallet.


The `PocketMoney` object represents money a user has in their pocket. For instance, User1 may have 100 BGN and 50 EUR. 
This is the source of the user for processing deposits to the wallet and the destination where the withdrawals are sent.

## 🚀 Set and Startup project

1. 🔍 Check `application.properties` and set your database `url`, `username` and `password`.
   You can find it in `/src/main/resources/application.properties`;
2. 🔍 Check `build.gradle` and review database dependency.
      You can find it in `supper-wallet/build.gradle`;
3. 🔍 Check `DbConfig.java` and set your `Datasource Driver`;
4. ⚙️ Set connection with the database and use `create.sql` and `insert.sql` to create the database and fill it with data.
   You can find them in `superwallet/db`.
5. 🔐 Basic Authentication is implemented. In HTTP Headers set Key: `Authorization` and Value: `username password`. Check `insert.sql` for a valid username and password.
6. ✉️ The email notification service is implemented using [MailJet](https://www.mailjet.com/). To set it up follow these steps:
   1. Register at https://www.mailjet.com/
   2. After login create your API and secret keys
   3. Go to `/src/main/resources/application.properties` and set up your API key and secret. For the secret key you need to create 
   an Environmental variable for extra security measures.
   4. Swap your personal API key and secret in the `MailJetConfig.java` class
7. 💲 The currency exchange service is implemented using [ExchangeRate-API](https://www.exchangerate-api.com/) To set it up follow these steps:
   1. Register at https://www.exchangerate-api.com/.
   2. After login create your personal API key.
   3. Go to `/src/main/resources/application.properties` and set up your API key.

## 📊 Database relations
You can find them in `super-wallet/db`.

## 💻 Technologies
* ☕️ Java
* 🌱 Spring Boot
* 💾 MariaDB
* 🌐 REST API
* 📦 Gradle

## ➡️ How to test it

The happy path: Kiril Kirilov has decided to save money for a trip to Japan. He has a few bucks in his pocket but he wants to transfer them to an online
wallet. He has already registered in the super wallet system, so the next step will be to create a new wallet. He gives it a name: "Japan trip"
and sets the currency to BGN. Later he remembered that this may not be the most appropriate currency, so he changed it to USD 
and the wallet's name to "Japan 2025". He also wants to get notified every time he makes a deposit and withdrawal so he sets this up too.
Now he has some pocket money in BGN, but the super wallet system supports automatic conversion between different currencies.
So he wires 100 BGN from his pocket money to his wallet and gives the deposit the name "First deposit for my Japan trip". Once that is done, he checks to see if a notification email is sent. Then sets the wallet's status to frozen.
for extra security. A few days later he decides to make another deposit but fails to do so because he forgot to change the wallet's status. Once the status is changed back to 
"Active", he can make the deposit. Some time passes, and he decides to buy a paper map of Japan, so he withdraws 10 USD from his wallet to his pocket money and gives the withdrawal the name "Paper Map Japan".
After that, he checks the transaction log history for his wallet, which includes all deposits and withdrawals from that particular wallet.

### 🌐 Endpoint: `GET /api/wallets/1`
Retrieves the wallet details for the wallet with ID `1`
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
---
### 🌐 Endpoint: `GET /api/wallets/1/balance`
Retrieves the balance of the wallet with ID `1`
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
---
### 🌐 Endpoint: `GET /api/wallets/6/transaction-history`
Get the transaction history logs of wallet with ID `6`
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
---
### 🌐 Endpoint: `POST /api/wallets`
Create a new wallet
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
### Sample Request Body:
```json
{
    "name": "Japan",
    "currencyCode": "BGN"
}
```
---
### 🌐 Endpoint: `PUT /api/wallets/6`
Updates the details of wallet with ID `6`
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
### Sample Request Body:
```json
{
  "name": "Japan 2025",
  "currencyCode": "USD",
  "statusId": "1",
  "depositNotifications": 1,
  "withdrawalNotifications": 1
}
```
---
### 🌐 Endpoint: `PATCH /api/wallets/1/deposit`
Make a deposit to a wallet with ID `1` if possible
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
### Sample Request Body:
```json
{
  "funds": 100.00,
  "pocketMoneyId": 4,
  "paymentDetails": "Sample payment details"
}
```
---
### 🌐 Endpoint: `PATCH /api/wallets/1/withdraw`
Make a withdrawal from a wallet with ID `1` if possible
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
### Sample Request Body:
```json
{
  "funds": 10.00,
  "pocketMoneyId": 4,
  "paymentDetails": "Sample payment details"
}
```
---
## 💳 Wallet `/api/wallets`

### GET
| Resource       | Endpoint                   | Description                       |
|----------------|----------------------------|-----------------------------------|
| Wallet         | `/{id}`                    | Get wallet by id                  |
| Wallet         | `/{id}/balance`            | Get wallet balance by id          |
| TransactionLog | `{id}/transaction-history` | Get transaction logs by wallet id |



### POST
| Resource         | Endpoint                                                          | Description                                      |
|------------------|-------------------------------------------------------------------|--------------------------------------------------|
| Wallet           |                                                                   | Create a new wallet                              |



### PUT
| Resource | Endpoint                                     | Description                                         |
|----------|----------------------------------------------|-----------------------------------------------------|
| Wallet   | `/{id}`                                      | Update wallet


### PATCH
| Resource | Endpoint                                              | Description                |
|----------|------------------------------------------------------|----------------------------|
| Wallet   | `/{id}/deposit`                                    | Deposit money to wallet    |
| Wallet  | `/{id}/withdraw`                                      | Withdraw money from wallet |
