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
     number numeric(4) not null,
     city varchar(24) not null,
     postalCode numeric(12) not null,
     province varchar(2) not null,
     email varchar(24) not null,
     constraint IDADDRESSES primary key (street, number, city),
     constraint IDADDRESSES_1_ID unique (email));

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
     address varchar(24) not null,
     completed boolean not null default false,
     constraint IDORDERS primary key (order_id),
     constraint IDORDERS_1 unique (address));

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
     surename varchar(24) not null,
     email varchar(24) not null,
     password varchar(24) not null,
     registrationDate date not null,
     role enum('CLIENT', 'STAFF', 'ADMIN') not null default 'CLIENT',
     blocked boolean not null default false,
     constraint IDUSERS primary key (email));


-- Constraints Section
-- ___________________ 

alter table ADDRESSES add constraint IDADDRESSES_1_FK
     foreign key (email)
     references USERS(email);

alter table NUTRITIONAL_INFOS add constraint IDNUTRITIONAL_INFOS_FK
     foreign key (product_name)
     references PRODUCTS(name);


-- Index Section
-- _____________ 

