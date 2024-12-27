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
  - A user can only update his wallet's information.
  - A user is able to change the `status` on their wallet to either *Active* of *Frozen*. A wallet with status set to *Frozen* can't
  make deposits and withdrawals.
  - The `currencyCode` of the wallet can be change only if the wallet's balance is equal to 0.
- `getWalletBalance()`
  - A user can only get the balance of their own wallets.
- `getWalletById()` - Gets the whole information about the wallet.
- `depositToWallet()`
  - Takes money from the user's `PocketMoney` and transfers them to his wallet of choice.
- `withdrawFromWallet()`
  - Take money from the user's wallet and transfers them to their `PocketMoney` of choice.


The `PocketMoney` object represents money you have in your pocket. For instance User1 may have 100 BGN and 50 EUR for instance. 
This is the source the user use for making deposits to the wallet and the destination where he withdraws money from the wallet.

## 🚀 Set and Start up project

1. 🔍 Check `application.properties` and set your personal database `url`, `username` and `password`.
   You can find it in `/src/main/resources/application.properties`;
2. 🔍 Check `build.gradle` and review database dependency.
      You can find it in `supper-wallet/build.gradle`;
3. 🔍 Check `DbConfig.java` and set your `Datasource Driver`;
4. ⚙️ Set connection with database and use `create.sql` and `insert.sql` to create the database and fill it with data.
   You can find them in `JobMatch/db`.
5. 🔐 Basic Authentication is implemented. In Http Headers set Key: `Authorization` and Value: `username password` - check `insert.sql` for valid username and password.

## 📊 Database relations
You can find it in `super-wallet/db`.

## 💻 Technologies
* ☕️ Java
* 🌱 Spring Boot
* 💾 MariaDB
* 🌐 REST API
* 📦 Gradle

## ➡️ How to test it

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
