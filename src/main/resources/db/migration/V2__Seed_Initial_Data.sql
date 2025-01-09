insert into currencies (currency_id, currency_code, currency_name, country)
values
    (1, 'BGN', 'Bulgarian Lev', 'Bulgaria'),
    (2, 'USD', 'United States Dollar', 'United States'),
    (3, 'EUR', 'Euro', 'European Union'),
    (4, 'GBP', 'British Pound Sterling', 'United Kingdom'),
    (5, 'JPY', 'Japanese Yen', 'Japan'),
    (6, 'CHF', 'Swiss Franc', 'Switzerland');

insert into users (user_id, username, password, first_name, last_name, email, date_of_birth, address)
values
    (1, 'petarpetrov', 'password', 'Petar', 'Petrov',
     'petarpetrov@gmail.com', '1978-01-12 00:00:00', 'ul. Pirotska 45, ap. 12, Sofia 1303, Bulgaria'),
    (2, 'markrober', 'password', 'Mark', 'Rober',
     'markrober@gmail.com','2000-04-01 00:00:00', '1234 Elm Street, Apt 56B, Springfield, IL 62701, USA'),
    (3, 'jeremyclarkson', 'password', 'Jeremy', 'Clarkson',
     'jeremyclarkson@gmail.com','1964-11-07 00:00:00', '123 Baker Street, London, W1U 6RP, United Kingdom'),
    (4, 'petergriffin', 'password', 'Peter', 'Grifin',
     'petergriffin@gmail.com','1999-01-31 00:00:00', '31 Spooner Street'),
    (5, 'kirilkirilov', 'password', 'Kiril', 'Kirilov',
     'kirilovmail90@gmail.com', '1990-11-09 00:00:00', '123 Java street, Sofia, 1000');


insert into pocket_money (pocket_money_id, amount, currency_id, user_id)
values
    (1, 10000.00, 1, 1),
    (2, 5000.00, 3, 1),
    (3, 250.00, 2, 2),
    (4, 200.00, 1, 5),
    (5, 1000000.00, 2, 2),
    (6, 550.00, 2, 1);

insert into statuses (status_id, status_name)
values
    (1, 'Active'),
    (2, 'Frozen');

insert into wallets (wallet_id, name, currency_id, user_id)
values
    (1, 'Main', 1, 1),
    (2, 'Savings', 3, 1),
    (3, 'Main', 2, 2),
    (4, 'Dream home', 4, 3),
    (5, 'Main', 1, 5);


