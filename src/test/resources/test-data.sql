INSERT into users (user_name, email) VALUES ('ValeraTestJPA', 'valera@mail.ru');
INSERT into users (user_name, email) VALUES ('ValeraTest2JPA', 'valera2@mail.ru');
INSERT into items (item_name, description, available, user_id)
VALUES ('ТабуретJPA', 'Такой табурет на 4 ножках', true, 1);
INSERT into bookings (start_dt, end_dt, item_id, booker, status)
VALUES (NOW() - 1, NOW() + 2, 1, 1, 1);
INSERT into bookings (start_dt, end_dt, item_id, booker, status)
VALUES (NOW() - 6, NOW() - 1, 1, 1, 1);
INSERT into bookings (start_dt, end_dt, item_id, booker, status)
VALUES (NOW() + 6, NOW() + 8, 1, 1, 1);
INSERT into bookings (start_dt, end_dt, item_id, booker, status)
VALUES (NOW() - 6, NOW() + 8, 1, 1, 3);






