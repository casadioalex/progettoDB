/*
    Query to authenticate users during login
*/
-- LOGIN
SELECT password FROM USERS WHERE email = ?;


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
SELECT order_id, completed, order_date FROM ORDERS WHERE user_email = ? ORDER BY completed ASC, order_date DESC;

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