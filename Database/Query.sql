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
SELECT * FROM ORDERS WHERE user_email = ? ORDER BY completed ASC, order_date DESC, order_id DESC;

/*
    Query to get all uncompleted orders
*/
-- GET_UNCOMPLETED_ORDERS
SELECT order_id
FROM ORDERS
WHERE completed = 0;

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
