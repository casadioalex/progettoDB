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

create table ADDRESS (
     street varchar(24) not null,
     number numeric(4) not null,
     city varchar(24) not null,
     postalCode numeric(12) not null,
     province varchar(2) not null,
     email varchar(24) not null,
     constraint IDADDRESS primary key (street, number, city),
     constraint IDADDRESS_1_ID unique (email));

create table ORDER_DETAILS (
     order_id int not null,
     product_name varchar(24) not null,
     quantity char(2) not null,
     constraint IDORDER_DETAILS primary key (order_id, product_name));

create table INGREDIENT (
     ingredient_id int not null auto_increment,
     name varchar(24) not null,
     constraint IDINGREDIENT primary key (ingredient_id));

create table NUTRITIONAL_INFO (
     product_name varchar(24) not null,
     calories numeric(5) not null,
     carbohydrates numeric(5) not null,
     proteins numeric(5) not null,
     fats numeric(5) not null,
     constraint IDNUTRITIONAL_INFO_ID primary key (product_name));

create table `ORDER` (
     order_id int not null auto_increment,
     price numeric(5,2) not null,
     order_date date not null,
     user_email varchar(24) not null,
     address varchar(24) not null,
     completed boolean not null default false,
     constraint IDORDER primary key (order_id),
     constraint IDORDER_1 unique (address));

create table PRODUCT (
     name varchar(24) not null,
     constraint IDPRODUCT primary key (name));

create table REVIEW (
     review_id int not null auto_increment,
     comment varchar(200) not null,
     vote numeric(1) not null,
     review_date date not null,
     constraint IDREVIEW primary key (review_id));

create table USER (
     username varchar(24) not null,
     name varchar(24) not null,
     surename varchar(24) not null,
     email varchar(24) not null,
     password varchar(24) not null,
     registrationDate date not null,
     role enum('CLIENT', 'STAFF', 'ADMIN') not null default 'CLIENT',
     blocked boolean not null default false,
     constraint IDUSER primary key (email));


-- Constraints Section
-- ___________________ 

alter table ADDRESS add constraint IDADDRESS_1_FK
     foreign key (email)
     references USER(email);

alter table NUTRITIONAL_INFO add constraint IDNUTRITIONAL_INFO_FK
     foreign key (product_name)
     references PRODUCT(name);


-- Index Section
-- _____________ 

