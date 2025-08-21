/*
    Query to authenticate users during login
*/
-- LOGIN
SELECT * FROM USERS WHERE email = ? AND password = ?;
