/*
    Query to authenticate users during login
*/
-- LOGIN
SELECT password, role FROM USERS WHERE email = ?;


/*
    Query to register new users
*/
-- REGISTER
INSERT INTO USERS (username, name, surname, email, password, registrationDate) VALUES (?, ?, ?, ?, ?, NOW());

/*
    Query to register the address of a new user
*/
-- REGISTER_ADDRESS
INSERT INTO ADDRESSES (street, number, city, postalCode, province, user_email) VALUES (?, ?, ?, ?, ?, ?);

/*
    Query to get all orders id 
*/
-- GET_ALL_ORDERS
SELECT * FROM ORDERS WHERE user_email = ? ORDER BY completed ASC, order_date DESC, order_id DESC;

/*
    Query to get all uncompleted orders
*/
-- GET_UNCOMPLETED_ORDERS
SELECT order_id
FROM ORDERS
WHERE completed = 0;

/*
    Query to get all details of a specific order
*/
-- GET_ORDER_DETAILS_BY_ORDER_ID
SELECT product_name, quantity
FROM ORDER_DETAILS
WHERE order_id = ?;

/*
    Query to mark an order as completed
*/
-- COMPLETE_ORDER
UPDATE ORDERS
SET completed = true
WHERE order_id = ?;

/*
    Query to get user role by email
*/
-- GET_USER_ROLE
SELECT role 
FROM USERS 
WHERE email = ?;

/*
    Query to get all staff members
*/
-- GET_ALL_STAFF
SELECT name, surname, email
FROM USERS
WHERE role = 'STAFF';

/*
    Query to register new staff members
*/
-- REGISTER_NEW_STAFF
INSERT INTO USERS (username, name, surname, email, password, registrationDate, role) VALUES (?, ?, ?, ?, ?, NOW(), 'STAFF');

/*
    Query to get staff details by email
*/
-- GET_STAFF_DETAIL_BY_EMAIL
SELECT username, name, surname, email, registrationDate
FROM USERS
WHERE email = ?;

/*
    Query to remove a staff member by email
*/
-- REMOVE_STAFF_BY_EMAIL
DELETE FROM USERS 
WHERE email = ?;

/*
    Query to get all products
*/
-- GET_ALL_PRODUCTS
SELECT * FROM PRODUCTS

/*
    Query to get ingredients of a product
*/
-- GET_PRODUCT_INGREDIENTS
SELECT ingredient_name FROM PRODUCT_INGREDIENTS WHERE product_name = ?;

/*
    Query to get nutritional information of a product
*/
-- GET_NUTRITIONAL_INFO
SELECT * FROM NUTRITIONAL_INFOS WHERE product_name = ?;

/*
    Query to get user address info for order
*/
-- GET_USER_ADDRESS_FOR_ORDER
SELECT street, number, city FROM ADDRESSES WHERE user_email = ?;

/*
    Query to create a new order
*/
-- CREATE_ORDER
INSERT INTO ORDERS (user_email, address_street, address_number, address_city, price, order_date) VALUES (?, ?, ?, ?, ?, NOW());

-- CREATE_ORDER_DETAILS
INSERT INTO ORDER_DETAILS (order_id, product_name, quantity) VALUES (?, ?, ?);

/*
    Query to get all users
*/
-- GET_ALL_USERS
SELECT username, name, surname, email, registrationDate, role, blocked
FROM USERS
WHERE role <> 'ADMIN';

/*
    Query to block a user by email
*/
-- BLOCK_USER
UPDATE USERS
SET blocked = true
WHERE email = ?;

/*
    Query to unblock a user by email
*/
-- UNBLOCK_USER
UPDATE USERS 
SET blocked = false 
WHERE email = ?

