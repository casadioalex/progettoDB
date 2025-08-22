-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Thu Aug 21 01:37:15 2025
-- ********************************************* 


-- Database Section
-- ________________ 
drop database if exists MCDONALD;

create database MCDONALD;
use MCDONALD;

-- DBSpace Section
-- _______________


-- Tables Section
-- _____________ 

create table ADDRESSES (
     street varchar(24) not null,
     number varchar(24) not null,
     city varchar(24) not null,
     postalCode varchar(24) not null,
     province varchar(24) not null,
     user_email varchar(24) not null,
     constraint IDADDRESSES primary key (street, number, city));

create table ORDER_DETAILS (
     order_id int not null,
     product_name varchar(24) not null,
     quantity char(2) not null,
     constraint IDORDER_DETAILS primary key (order_id, product_name));

create table INGREDIENTS (
     ingredient_id int not null auto_increment,
     name varchar(24) not null,
     constraint IDINGREDIENTS primary key (ingredient_id));

create table NUTRITIONAL_INFOS (
     product_name varchar(24) not null,
     calories numeric(5) not null,
     carbohydrates numeric(5) not null,
     proteins numeric(5) not null,
     fats numeric(5) not null,
     constraint IDNUTRITIONAL_INFOS_ID primary key (product_name));

create table ORDERS (
     order_id int not null auto_increment,
     price numeric(5,2) not null,
     order_date date not null,
     user_email varchar(24) not null,
     street varchar(24) not null,
     number varchar(24) not null,
     city varchar(24) not null,
     completed boolean not null default false,
     constraint IDORDERS primary key (order_id));

create table PRODUCTS (
     name varchar(24) not null,
     constraint IDPRODUCTS primary key (name));

create table REVIEWS (
     review_id int not null auto_increment,
     comment varchar(200) not null,
     vote numeric(1) not null,
     review_date date not null,
     constraint IDREVIEWS primary key (review_id));

create table USERS (
     username varchar(24) not null,
     name varchar(24) not null,
     surname varchar(24) not null,
     email varchar(24) not null,
     password varchar(256) not null,
     registrationDate date not null,
     role enum('CLIENT', 'STAFF', 'ADMIN') not null default 'CLIENT',
     blocked boolean not null default false,
     constraint IDUSERS primary key (email));


-- Constraints Section
-- ___________________ 

alter table ADDRESSES add constraint IDADDRESSES_1_FK
     foreign key (user_email)
     references USERS(email);

alter table NUTRITIONAL_INFOS add constraint IDNUTRITIONAL_INFOS_FK
     foreign key (product_name)
     references PRODUCTS(name);

alter table ORDERS add constraint IDORDERS_1_FK
     foreign key (street, number, city)
     references ADDRESSES(street, number, city);


-- Index Section
-- _____________ 

-- USERS table population
INSERT INTO USERS (username, name, surname, email, password, registrationDate, role, blocked) VALUES
('mrossi', 'Mario', 'Rossi', 'mario.rossi@email.com', 'c4cb8b3fae0fa9eabf8a91e84b1c7cfcfa2c3e0b1b2c2a1a3e6a0a1a5e6a2e3e', '2024-01-10', 'CLIENT', false),
('lbianchi', 'Luca', 'Bianchi', 'luca.bianchi@email.com', '6cb75f652a9b52798eb6cf2201057c73e0679d741c7c252f7d7c1e03c6d5e7c6', '2024-02-15', 'STAFF', false),
('admin', 'Admin', 'McDonald', 'admin@mcdonald.com', '713bfda78870bf9d1b261f565286f85e97ee614efe5f0faf7c34e7ca4f65baca', '2023-12-01', 'ADMIN', false);

-- ADDRESSES table population
INSERT INTO ADDRESSES (street, number, city, postalCode, province, user_email) VALUES
('Via Roma', 10, 'Milano', 20100, 'MI', 'mario.rossi@email.com'),
('Via Roma', 5, 'Roma', 00100, 'RM', 'luca.bianchi@email.com'),
('Corso Italia', 5, 'Roma', 00100, 'RM', 'luca.bianchi@email.com');

-- PRODUCTS table population
INSERT INTO PRODUCTS (name) VALUES
('Big Mac'),
('McChicken'),
('Patatine');

-- INGREDIENTS table population
INSERT INTO INGREDIENTS (name) VALUES
('Pane'),
('Carne'),
('Insalata'),
('Formaggio'),
('Patate');

-- NUTRITIONAL_INFOS table population
INSERT INTO NUTRITIONAL_INFOS (product_name, calories, carbohydrates, proteins, fats) VALUES
('Big Mac', 500, 45, 25, 28),
('McChicken', 400, 40, 20, 18),
('Patatine', 300, 35, 4, 15);

-- ORDERS table population
INSERT INTO ORDERS (price, order_date, user_email, street, number, city, completed) VALUES
(8.50, '2024-08-01', 'mario.rossi@email.com', 'Via Roma', 10, 'Milano', true),
(6.00, '2024-08-02', 'luca.bianchi@email.com', 'Corso Italia', 5, 'Roma', false),
(8.50, '2024-08-01', 'luca.bianchi@email.com', 'Via Roma', 5, 'Roma', false);

-- ORDER_DETAILS table population
INSERT INTO ORDER_DETAILS (order_id, product_name, quantity) VALUES
(1, 'Big Mac', '2'),
(1, 'Patatine', '1'),
(2, 'McChicken', '1');

-- REVIEWS table population
INSERT INTO REVIEWS (comment, vote, review_date) VALUES
('Ottimo servizio!', 5, '2024-08-03'),
('Panino buono ma attesa lunga.', 3, '2024-08-04');