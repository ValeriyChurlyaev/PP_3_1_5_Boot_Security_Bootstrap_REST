insert into roles (name)
values
('ROLE_ADMIN'),
('ROLE_USER');



insert into users_roles (user_id, role_id)
values
(1, 1),
(1, 2),
(2, 2);


insert into users (first_name, age, password, email, last_name)
values
('oleg', 20, '$2a$12$/8t9fbAItRcCeG8aBKzBjeQcaI1w5zWjyA8Oc2zJKwGPhCrT4keL2', 'oleg100@mail.ru', 'Second100'),
('igor', 10, '$2a$12$2aYmp9akKieoRaQ.zpb8ruudNdipcez7HOQi8T4/o3ehbnFR6HJnG', 'igorXXX@gmail.com', 'Second200');