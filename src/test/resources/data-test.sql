INSERT INTO _user (user_id, first_name, last_name, phone_number, address, email, password, role)
    VALUES (1, 'Tom', 'Smith', '123456789', 'Warsaw', 'test@customer.com', 'test', 'CUSTOMER');
INSERT INTO customer (user_id) VALUES (1);
INSERT INTO _user (user_id, first_name, last_name, phone_number, address, email, password, role)
    VALUES (2, 'John', 'Snow', '121451759', 'Warsaw', 'testsecond@customer.com', 'test', 'CUSTOMER');
INSERT INTO customer (user_id) VALUES (2);

INSERT INTO _user (user_id, first_name, last_name, phone_number, address, email, password, role)
    VALUES (3, 'Jan', 'Test', '123456789', 'Warsaw, Emilii Plater 1234', 'test@employee.com', crypt('test', gen_salt('bf')), 'EMPLOYEE');
INSERT INTO employee (employed_from, salary, user_id) VALUES ('2024-06-19 10:00', 7000, 3);
