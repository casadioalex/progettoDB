-- Popolamento tabella USER
INSERT INTO USER (username, name, surename, email, password, registrationDate, role, blocked) VALUES
('mrossi', 'Mario', 'Rossi', 'mario.rossi@email.com', 'password1', '2024-01-10', 'CLIENT', false),
('lbianchi', 'Luca', 'Bianchi', 'luca.bianchi@email.com', 'password2', '2024-02-15', 'STAFF', false),
('admin', 'Admin', 'McDonald', 'admin@mcdonald.com', 'adminpass', '2023-12-01', 'ADMIN', false);

-- Popolamento tabella ADDRESS
INSERT INTO ADDRESS (street, number, city, postalCode, province, email) VALUES
('Via Roma', 10, 'Milano', 20100, 'MI', 'mario.rossi@email.com'),
('Corso Italia', 5, 'Roma', 00100, 'RM', 'luca.bianchi@email.com');

-- Popolamento tabella PRODUCT
INSERT INTO PRODUCT (name) VALUES
('Big Mac'),
('McChicken'),
('Patatine');

-- Popolamento tabella INGREDIENT
INSERT INTO INGREDIENT (name) VALUES
('Pane'),
('Carne'),
('Insalata'),
('Formaggio'),
('Patate');

-- Popolamento tabella NUTRITIONAL_INFO
INSERT INTO NUTRITIONAL_INFO (product_name, calories, carbohydrates, proteins, fats) VALUES
('Big Mac', 500, 45, 25, 28),
('McChicken', 400, 40, 20, 18),
('Patatine', 300, 35, 4, 15);

-- Popolamento tabella ORDER
INSERT INTO `ORDER` (price, order_date, user_email, address, completed) VALUES
(8.50, '2024-08-01', 'mario.rossi@email.com', 'Via Roma', true),
(6.00, '2024-08-02', 'luca.bianchi@email.com', 'Corso Italia', false);

-- Popolamento tabella ORDER_DETAILS
INSERT INTO ORDER_DETAILS (order_id, product_name, quantity) VALUES
(1, 'Big Mac', '2'),
(1, 'Patatine', '1'),
(2, 'McChicken', '1');

-- Popolamento tabella REVIEW
INSERT INTO REVIEW (comment, vote, review_date) VALUES
('Ottimo servizio!', 5, '2024-08-03'),
('Panino buono ma attesa lunga.', 3, '2024-08-04');