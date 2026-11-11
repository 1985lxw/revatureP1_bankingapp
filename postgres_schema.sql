-- NOTES:
-- This is version 1 of the schema for the postgres part of the project
-- Subject to change!!


-- Corresponds to the Customer model
create table customers (
	id int generated always as identity primary key,
	name text not null,
	username text not null unique,
	hashed_password text not null
);


-- Corresponds to the BankAccount model 
-- Project doc expects 2 account types (checking, savings) so we have a column for that
-- Accounts are linked to a customer through foreign key
create table bank_accounts (
	account_id int generated always as identity primary key,
	owner_id int,
	account_type text not null check (account_type in ('CHECKINGS', 'SAVINGS')),
	balance numeric(10, 2) not null check (balance >= 0),
	foreign key (owner_id) references customers(id)
);


-- Corresponds to Transaction model
-- account_id and other_account_id are the 2 accounts which a transaction is being made between
create table transactions (
	transaction_id int generated always as identity primary key,
	account_id int not null,
	other_account_id int,
	transaction_type text not null check (transaction_type in ('DEPOSIT', 'WITHDRAWAL', 'OUTBOUND', 'INBOUND')),
	amount numeric(10, 2) not null check (amount > 0),
	balance_after numeric(10, 2) not null check (balance_after >= 0),
	created_at timestamp not null default now(),
	
	foreign key (account_id) references bank_accounts(account_id),
	foreign key (other_account_id) references bank_accounts(account_id)
);


-- Queries for testing purposes
-- select * from customers;
-- select * from bank_accounts;
-- select * from transactions;

-- drop table customers;
-- drop table bank_accounts;
-- drop table transactions;