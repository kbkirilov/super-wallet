# super-wallet

## 💡 Description

SupperWallet is simple API that allows users to interact with a virtual `Wallet`. It is build according to the layered architecture style, where we are layers for the controllers, services and repositories. <br>

Each `User` can have multiple wallets as his possession. 

Each `Wallet` can have a `name`, `balance`, `currencyCode` (ISO 4217) and `status`. 
The user can `deposit()`/`withdraw()` money to/from their wallets. These are some of the operations the user can make with a wallet:

- `createWallet()`
  - A user can't have two wallets with the same name, but two wallets with the same name can exist in the database.
  - Each wallet can be created with a given currency. At the moment the database support the currencies of the 6
    strongest economies.
- `updateWallet()` 
  - The user can only update his own wallet's information.
  - The user is able to change the `status` on their wallet to either *Active* of *Frozen*. A wallet with status set to *Frozen* can't
  make deposits and withdrawals.
  - The user can update the `depositNotifications` and `withdrawalNotifications` fields if he wants to receive email notifications when making a deposits/withdrawals.
  - The `currencyCode` of the wallet can be change only if the wallet's balance is equal to 0.
- `getWalletBalance()`
  - A user can only get the balance of their own wallets.
- `getWalletById()` - Gets the whole information about the wallet.
- `depositToWallet()`
  - Takes money from the user's `PocketMoney` and transfers them to his wallet of choice.
  - If `depositNotifications` is set to 1 the user receives an email notification every time he deposits money from his pocket to his wallet.
- `withdrawFromWallet()`
  - Take money from the user's wallet and transfers them to their `PocketMoney` of choice.
  - If `withdrawalNotifications` is set to 1 the user receives an email notification every time he withdraws money from his wallet to his pocket money.


The `PocketMoney` object represents money you have in your pocket. For instance User1 may have 100 BGN and 50 EUR for instance. 
This is the source the user use for making deposits to the wallet and the destination where he withdraws money from the wallet.

## 🚀 Set and Start up project

1. 🔍 Check `application.properties` and set your personal database `url`, `username` and `password`.
   You can find it in `/src/main/resources/application.properties`;
2. 🔍 Check `build.gradle` and review database dependency.
      You can find it in `supper-wallet/build.gradle`;
3. 🔍 Check `DbConfig.java` and set your `Datasource Driver`;
4. ⚙️ Set connection with database and use `create.sql` and `insert.sql` to create the database and fill it with data.
   You can find them in `superwallet/db`.
5. 🔐 Basic Authentication is implemented. In Http Headers set Key: `Authorization` and Value: `username password` - check `insert.sql` for valid username and password.
6. ✉️ The email notification service is implemented using [MailJet](https://www.mailjet.com/). To set it up follow these steps:
   1. Register at https://www.mailjet.com/
   2. After login check your personal API key and secret
   3. Go to `/src/main/resources/application.properties` and set up your API key and secret. For the secret key you need to create 
   an Environmental variable for extra security measures.
   4. Swap your personal API key and secret in the `MailJetConfig.java` class

## 📊 Database relations
You can find them in `super-wallet/db`.

## 💻 Technologies
* ☕️ Java
* 🌱 Spring Boot
* 💾 MariaDB
* 🌐 REST API
* 📦 Gradle

## ➡️ How to test it

The happy path: Kitodar Todorov has decided to save money for a trip to Kosovo. He has got a few bucks in his pocket, but he wants to transfer them to an online
wallet. He has already registered in the super wallet system, so the next step will be to create a new wallet. He gives it a name: "Kosovo trip"
and sets the currency to BGN. Later he remembers that this may not be the most appropriate currency, so he changes it to USD 
and the wallet's name to "Summer 2025". Now he transfers a few dollar from his pocket to the wallet. Once that is done, he sets the wallet's status to frozen.
for extra security. A few days later he decides to make another deposit, but fails to do so because he forgot to change the wallet's status. Once the status is changed back to 
active, he can make the deposit. 

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
### 🌐 Endpoint: `POST /api/wallets`
Create a new wallet
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
### Sample Request Body:
```json
{
    "name": "New House",
    "currencyCode": "EUR"
}
```
---
### 🌐 Endpoint: `PUT /api/wallets/1`
Updates the details of wallet with ID `1`
### Required Headers:
- **Authorization**: **Key**: *Authorization* **Value**: *username password*
### Sample Request Body:
```json
{
  "name": "Friday's cinema",
  "currencyCode": "BGN",
  "statusId": "1"
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
  "funds": 50.00,
  "pocketMoneyId": 1
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
  "funds": 20.00,
  "pocketMoneyId": 1
}
```
---
## 💳 Wallet `/api/wallets`

### GET
| Resource         | Endpoint                               | Description                                                      |
|------------------|----------------------------------------|------------------------------------------------------------------|
| Wallet           | `/{id}`                                | Get wallet by id                                                 |
| Wallet           | `/{id}/balance`                        | Get wallet balance by id                                         |



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
