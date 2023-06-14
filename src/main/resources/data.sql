INSERT INTO car (brand, model, production_year, actual_daily_price) values ('toyota', 'corolla', 2020, 100);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('kia', 'sportage', 2019, 250);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('volkswagen', 'polo', 2018, 150);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('toyota', 'yaris', 2021, 110);
INSERT INTO car (brand, model, production_year, actual_daily_price) values ('volkswagen', 'golf', 2018, 80);

INSERT INTO price_update (price, update_date, car_id) VALUES (100, '2023-05-23 11:00', 1);
INSERT INTO price_update (price, update_date, car_id) VALUES (250, '2023-05-23 11:00', 2);
INSERT INTO price_update (price, update_date, car_id) VALUES (150, '2023-05-23 11:00', 3);
INSERT INTO price_update (price, update_date, car_id) VALUES (110, '2023-05-23 11:00', 4);
INSERT INTO price_update (price, update_date, car_id) VALUES (80, '2023-05-23 11:00', 5);

INSERT INTO customer (first_name, last_name, phone_number, address, email, password) values ('Jan', 'Kowalski', '123456789', 'Warszawa, Emilii Plater 24', 'jan.kowalski@gmail.com', 'zaq12wsx');
INSERT INTO customer (first_name, last_name, phone_number, address, email, password) values ('Piotr', 'Nowak', '111222333', 'Warszawa, Niepodległości 44', 'piotr.nowak@gmail.com', '12345');
INSERT INTO customer (first_name, last_name, phone_number, address, email, password) values ('Karolina', 'Wójcik', '987654321', 'Gdańsk, 3 Maja 56', 'karolina.wojcik@o2.pl', 'wsxcde3');
INSERT INTO customer (first_name, last_name, phone_number, address, email, password) values ('Oskar', 'Stankowski', '666999777', 'Warszawa, Sienkiewicza 25', 'oskar.stan@gmail.com', '12345');
INSERT INTO customer (first_name, last_name, phone_number, address, email, password) values ('Paulina', 'Nowak', '801802803', 'Pruszków, Batorego 33', 'paula.nowak@protonmail.com', 'AwX0!~lmn ..;');

INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-19 10:00', '2023-03-27 10:00', 1, 1);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-29 10:00', '2023-04-04 10:00', 1, 5);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-04-08 07:00', '2023-04-14 14:00', 1, 2);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-04-27 09:00', '2023-05-09 09:00', 1, 1);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-17 10:00', '2023-03-20 10:00', 2, 2);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-03-04 10:00', '2023-03-24 10:00', 3, 3);
INSERT INTO rental (from_date, to_date, car_id, customer_id) values ('2023-05-05 14:00', '2023-05-15 14:00', 3, 3);
