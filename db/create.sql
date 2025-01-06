create table currencies
(
    currency_id int auto_increment primary key ,
    currency_code char(3) not null unique ,
    currency_name varchar(30) not null unique ,
    country varchar(30) not null
);

create table users
(
    user_id int auto_increment primary key ,
    username varchar(20) not null unique ,
    password varchar(15) not null ,
    first_name varchar(20) not null ,
    last_name varchar(20) not null ,
    email varchar(50) not null ,
    date_of_birth datetime not null ,
    address varchar(200) not null
);

create table pocket_money
(
    pocket_money_id int auto_increment primary key ,
    amount decimal(15,2) not null ,
    currency_id int ,
    user_id int ,

    constraint fk_pocket_money_currency_id
        foreign key (currency_id) references currencies (currency_id) ,
    constraint fk_pocket_money_user_id
        foreign key (user_id) references users (user_id)
);

create table statuses
(
    status_id int auto_increment primary key ,
    status_name varchar(20) unique
);

create table wallets
(
    wallet_id int auto_increment primary key ,
    name varchar(40) not null ,
    currency_id int ,
    user_id int ,
    status_id int default 1,
    balance decimal(15,2) not null default 0 ,
    created_at datetime default current_timestamp ,
    deposit_notifications boolean default true ,
    withdrawal_notifications boolean default true ,

    constraint fk_wallets_currency_id
        foreign key (currency_id) references currencies (currency_id) ,
    constraint fk_wallets_user_id
        foreign key (user_id) references users (user_id) ,
    constraint fk_wallets_status_id
        foreign key (status_id) references statuses (status_id) ,
    constraint unique_user_wallet_name
        unique (user_id, name)
);