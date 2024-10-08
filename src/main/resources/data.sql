INSERT INTO car (brand, model, production_year, actual_daily_price) values ('toyota', 'corolla', 2020, 100);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('kia', 'sportage', 2019, 220);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('volkswagen', 'polo', 2018, 150);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('toyota', 'yaris', 2021, 90);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('volkswagen', 'golf', 2018, 80);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('ford', 'focus', 2018, 110);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('toyota', 'aygo', 2019, 120);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('volkswagen', 'passat', 2016, 120);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('ford', 'fiesta', 2016, 80);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('volkswagen', 'golf', 2017, 80);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('toyota', 'yaris', 2020, 90);

INSERT INTO price_update (price, update_date, car_id) VALUES (80, '2023-02-23 11:00', 1);
INSERT INTO price_update (price, update_date, car_id) VALUES (150, '2023-02-23 11:00', 2);
INSERT INTO price_update (price, update_date, car_id) VALUES (150, '2023-02-23 11:00', 3);
INSERT INTO price_update (price, update_date, car_id) VALUES (110, '2023-02-23 11:00', 4);
INSERT INTO price_update (price, update_date, car_id) VALUES (90, '2023-02-23 11:00', 5);
INSERT INTO price_update (price, update_date, car_id) VALUES (100, '2023-02-23 11:00', 6);
INSERT INTO price_update (price, update_date, car_id) VALUES (70, '2023-02-23 11:00', 7);
INSERT INTO price_update (price, update_date, car_id) VALUES (110, '2023-02-23 11:00', 8);

INSERT INTO _user (first_name, last_name, phone_number, address, email, password, role) values ('Jan', 'Kowalski', '123456789', 'Warszawa, Emilii Plater 24', 'jan.kowalski@gmail.com', crypt('zaq12wsx', gen_salt('bf')), 'CUSTOMER');
INSERT INTO _user (first_name, last_name, phone_number, address, email, password, role) values ('Piotr', 'Nowak', '111222333', 'Warszawa, Niepodległości 44', 'piotr.nowak@gmail.com',  crypt('12345', gen_salt('bf')), 'CUSTOMER');
-- INSERT INTO _user (first_name, last_name, phone_number, address, email, password, role) values ('Jan', 'Kowalski', '123456789', 'Warszawa, Emilii Plater 24', 'jan.kowalski@gmail.com', 'zaq12wsx', 'CUSTOMER');
-- INSERT INTO _user (first_name, last_name, phone_number, address, email, password, role) values ('Piotr', 'Nowak', '111222333', 'Warszawa, Niepodległości 44', 'piotr.nowak@gmail.com',  '12345', 'CUSTOMER');

INSERT INTO _user (first_name, last_name, phone_number, address, email, password) values ('Karolina', 'Wójcik', '987654321', 'Gdańsk, 3 Maja 56', 'karolina.wojcik@o2.pl', crypt('wsxcde3', gen_salt('bf')));
INSERT INTO _user (first_name, last_name, phone_number, address, email, password) values ('Oskar', 'Stankowski', '666999777', 'Warszawa, Sienkiewicza 25', 'oskar.stan@gmail.com', crypt('12345', gen_salt('bf')));
INSERT INTO _user (first_name, last_name, phone_number, address, email, password) values ('Paulina', 'Nowak', '801802803', 'Pruszków, Batorego 33', 'paula.nowak@protonmail.com', crypt('AwX0!~lmn ..;', gen_salt('bf')));

INSERT INTO customer (user_id) VALUES (1);
INSERT INTO customer (user_id) VALUES (2);
INSERT INTO customer (user_id) VALUES (3);
INSERT INTO customer (user_id) VALUES (4);
INSERT INTO customer (user_id) VALUES (5);

INSERT INTO _user (first_name, last_name, phone_number, address, email, password, role) values ('Jakub', 'Creighton', '666123009', 'Warszawa, Niepodległości 32', 'jakub.creighton@carrental.com', crypt('jakub', gen_salt('bf')), 'EMPLOYEE');
INSERT INTO employee (user_id, employed_from, salary) values (6, '2023-06-24', 4000);

INSERT INTO _user (first_name, last_name, phone_number, address, email, password, role) values ('Admin', 'Admin', '983919000', 'Warszawa, Marszałkowska 21', 'admin@carrental.com', crypt('admin', gen_salt('bf')), 'ADMIN');

INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-19 10:00', '2023-03-27 10:00', 1, 1);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-29 10:00', '2023-04-04 10:00', 1, 5);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-04-08 07:00', '2023-04-14 14:00', 1, 2);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-04-27 09:00', '2023-05-09 09:00', 1, 1);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-17 10:00', '2023-03-20 10:00', 2, 2);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-04 10:00', '2023-03-24 10:00', 3, 3);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-05-05 14:00', '2023-05-15 14:00', 3, 3);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-08-05 10:00', '2023-08-15 10:00', 5, 3);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-08-20 10:00', '2023-08-30 10:00', 8, 4);

INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2024-04-20 10:00', '2024-05-10 10:00', 2, 2);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2024-04-20 10:00', '2024-04-30 10:00', 3, 4);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2024-05-20 10:00', '2024-08-30 10:00', 8, 5);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2024-02-17 10:00', '2024-08-21 10:00', 1, 1);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2024-06-20 10:00', '2024-06-30 10:00', 2, 3);