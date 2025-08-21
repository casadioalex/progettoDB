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