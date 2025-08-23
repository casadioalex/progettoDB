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
     quantity int not null,
     constraint IDORDER_DETAILS primary key (order_id, product_name));

create table INGREDIENTS (
     name varchar(24) not null,
     constraint IDINGREDIENTS primary key (name));

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
     address_street varchar(24) not null,
     address_number varchar(24) not null,
     address_city varchar(24) not null,
     completed boolean not null default false,
     constraint IDORDERS primary key (order_id));

create table PRODUCTS (
     name varchar(24) not null,
     price numeric(5,2) not null,
     constraint IDPRODUCTS primary key (name));

create table PRODUCT_INGREDIENTS (
     product_name varchar(24) not null,
     ingredient_name varchar(24) not null,
     constraint IDPRODUCT_INGREDIENTS primary key (product_name, ingredient_name));

create table REVIEWS (
     review_id int not null auto_increment,
     comment varchar(200) not null,
     vote numeric(1) not null,
     review_date date not null,
     user_email varchar(24) not null,
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
     references USERS(email) on delete cascade;

alter table ORDERS add constraint FK_ORDERS_ADDRESSES
     foreign key (address_street, address_number, address_city)
     references ADDRESSES(street, number, city);

alter table ORDERS add constraint IDORDERS_FK
     foreign key (user_email)
     references USERS(email);

alter table ORDER_DETAILS add constraint IDORDER_DETAILS_FK
     foreign key (order_id)
     references ORDERS(order_id);

alter table ORDER_DETAILS add constraint IDORDER_DETAILS_1_FK
     foreign key (product_name)
     references PRODUCTS(name);

alter table NUTRITIONAL_INFOS add constraint IDNUTRITIONAL_INFOS_FK
     foreign key (product_name)
     references PRODUCTS(name);

alter table REVIEWS add constraint IDREVIEWS_FK
     foreign key (user_email)
     references USERS(email);

alter table PRODUCT_INGREDIENTS add constraint FK_PRODUCT_INGREDIENTS_PRODUCTS
     foreign key (product_name)
     references PRODUCTS(name);

alter table PRODUCT_INGREDIENTS add constraint FK_PRODUCT_INGREDIENTS_INGREDIENTS
     foreign key (ingredient_name)
     references INGREDIENTS(name);


-- Index Section
-- _____________ 

-- USERS table population
INSERT INTO USERS (username, name, surname, email, password, registrationDate, role, blocked) VALUES
('mrossi', 'Mario', 'Rossi', 'mario.rossi@email.com', 'c4cb8b3fae0fa9eabf8a91e84b1c7cfcfa2c3e0b1b2c2a1a3e6a0a1a5e6a2e3e', '2024-01-10', 'CLIENT', false),
('lbianchi', 'Luca', 'Bianchi', 'luca.bianchi@email.com', '6cb75f652a9b52798eb6cf2201057c73e0679d741c7c252f7d7c1e03c6d5e7c6', '2024-02-15', 'STAFF', false),
('admin', 'Admin', 'McDonald', 'admin@mcdonald.com', '713bfda78870bf9d1b261f565286f85e97ee614efe5f0faf7c34e7ca4f65baca', '2023-12-01', 'ADMIN', false),
('gverdi', 'Giulia', 'Verdi', 'giulia.verdi@email.com', 'passwordhash1', '2024-03-20', 'CLIENT', false),
('pneri', 'Paolo', 'Neri', 'paolo.neri@email.com', 'passwordhash2', '2024-05-11', 'CLIENT', true),
('agialli', 'Anna', 'Gialli', 'anna.gialli@email.com', 'passwordhash3', '2024-06-01', 'STAFF', false);

-- ADDRESSES table population
INSERT INTO ADDRESSES (street, number, city, postalCode, province, user_email) VALUES
('Via Roma', '10', 'Milano', '20121', 'MI', 'mario.rossi@email.com'),
('Corso Vittorio Emanuele', '22', 'Milano', '20122', 'MI', 'mario.rossi@email.com'),
('Via del Corso', '101', 'Roma', '00186', 'RM', 'luca.bianchi@email.com'),
('Piazza di Spagna', '5', 'Roma', '00187', 'RM', 'luca.bianchi@email.com'),
('Via Toledo', '256', 'Napoli', '80132', 'NA', 'giulia.verdi@email.com'),
('Via Indipendenza', '8', 'Bologna', '40121', 'BO', 'paolo.neri@email.com');

-- PRODUCTS table population
INSERT INTO PRODUCTS (name, price) VALUES
('Big Mac', 5.50),
('McChicken', 4.80),
('Cheeseburger', 2.00),
('Chicken McNuggets 6pz', 4.50),
('Patatine Fritte Medie', 2.50),
('Coca-Cola 0.5L', 2.80),
('Acqua Naturale 0.5L', 1.50),
('McFlurry Oreo', 3.20);

-- INGREDIENTS table population
INSERT INTO INGREDIENTS (name) VALUES
('Pane al sesamo'),
('Carne bovina'),
('Insalata Iceberg'),
('Formaggio Cheddar'),
('Salsa Big Mac'),
('Cetriolini'),
('Cipolla'),
('Pane'),
('Pollo'),
('Maionese'),
('Patate'),
('Olio per frittura'),
('Sale'),
('Biscotti Oreo'),
('Gelato Fiordilatte'),
('Acqua'),
('Zucchero'),
('Anidride Carbonica');

-- PRODUCT_INGREDIENTS table population
INSERT INTO PRODUCT_INGREDIENTS (product_name, ingredient_name) VALUES
('Big Mac', 'Pane al sesamo'),
('Big Mac', 'Carne bovina'),
('Big Mac', 'Insalata Iceberg'),
('Big Mac', 'Formaggio Cheddar'),
('Big Mac', 'Salsa Big Mac'),
('Big Mac', 'Cetriolini'),
('Big Mac', 'Cipolla'),
('McChicken', 'Pane al sesamo'),
('McChicken', 'Pollo'),
('McChicken', 'Insalata Iceberg'),
('McChicken', 'Maionese'),
('Cheeseburger', 'Pane'),
('Cheeseburger', 'Carne bovina'),
('Cheeseburger', 'Formaggio Cheddar'),
('Cheeseburger', 'Cetriolini'),
('Cheeseburger', 'Cipolla'),
('Chicken McNuggets 6pz', 'Pollo'),
('Patatine Fritte Medie', 'Patate'),
('Patatine Fritte Medie', 'Olio per frittura'),
('Patatine Fritte Medie', 'Sale'),
('McFlurry Oreo', 'Gelato Fiordilatte'),
('McFlurry Oreo', 'Biscotti Oreo'),
('Coca-Cola 0.5L', 'Acqua'),
('Coca-Cola 0.5L', 'Zucchero'),
('Coca-Cola 0.5L', 'Anidride Carbonica'),
('Acqua Naturale 0.5L', 'Acqua');

-- NUTRITIONAL_INFOS table population
INSERT INTO NUTRITIONAL_INFOS (product_name, calories, carbohydrates, proteins, fats) VALUES
('Big Mac', 503, 42, 26, 25),
('McChicken', 433, 41, 22, 20),
('Cheeseburger', 301, 33, 15, 12),
('Chicken McNuggets 6pz', 269, 16, 15, 16),
('Patatine Fritte Medie', 330, 42, 4, 16),
('Coca-Cola 0.5L', 210, 53, 0, 0),
('Acqua Naturale 0.5L', 0, 0, 0, 0),
('McFlurry Oreo', 341, 54, 6, 11);

-- ORDERS table population
INSERT INTO ORDERS (price, order_date, user_email, address_street, address_number, address_city, completed) VALUES
(10.80, '2025-08-10', 'mario.rossi@email.com', 'Via Roma', '10', 'Milano', true),
(12.30, '2025-08-11', 'luca.bianchi@email.com', 'Via del Corso', '101', 'Roma', true),
(9.50, '2025-08-12', 'giulia.verdi@email.com', 'Via Toledo', '256', 'Napoli', false),
(5.50, '2025-08-15', 'mario.rossi@email.com', 'Corso Vittorio Emanuele', '22', 'Milano', true),
(15.00, '2025-08-20', 'luca.bianchi@email.com', 'Piazza di Spagna', '5', 'Roma', false);

-- ORDER_DETAILS table population
INSERT INTO ORDER_DETAILS (order_id, product_name, quantity) VALUES
(1, 'McChicken', 1),
(1, 'Patatine Fritte Medie', 1),
(1, 'Coca-Cola 0.5L', 1),
(2, 'Big Mac', 1),
(2, 'Chicken McNuggets 6pz', 1),
(2, 'Acqua Naturale 0.5L', 1),
(3, 'Cheeseburger', 2),
(3, 'Patatine Fritte Medie', 1),
(3, 'McFlurry Oreo', 1),
(4, 'Big Mac', 1),
(5, 'Big Mac', 1),
(5, 'McChicken', 1),
(5, 'Patatine Fritte Medie', 2);

-- REVIEWS table population
INSERT INTO REVIEWS (comment, vote, review_date, user_email) VALUES
('Ottimo servizio! Panino arrivato caldo e in orario.', 5, '2025-08-11', 'mario.rossi@email.com'),
('Panino buono ma attesa un po'' lunga.', 3, '2025-08-12', 'luca.bianchi@email.com'),
('Il mio ordine non è ancora stato completato, ma il sito è facile da usare.', 4, '2025-08-13', 'giulia.verdi@email.com'),
('Tutto perfetto come sempre, una garanzia!', 5, '2025-08-16', 'mario.rossi@email.com'),
('Servizio veloce e cibo caldo.', 5, '2025-08-17', 'luca.bianchi@email.com'),
('La qualità del cibo è sempre eccellente.', 5, '2025-08-18', 'giulia.verdi@email.com'),
('Purtroppo, ho trovato un capello nel panino.', 1, '2025-08-19', 'paolo.neri@email.com'),
('Il gelato era fuso e la coca-cola calda.', 2, '2025-08-20', 'anna.gialli@email.com');