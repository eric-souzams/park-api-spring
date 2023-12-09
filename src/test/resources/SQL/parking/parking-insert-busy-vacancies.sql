insert into users (id, username, password, role) values (100, 'maria@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_ADMIN');
insert into users (id, username, password, role) values (200, 'joao@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (300, 'lucas@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (400, 'mateus@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');

insert into clients (id, name, cpf, user_id) values (10, 'Joao', '85212472067', 200);
insert into clients (id, name, cpf, user_id) values (20, 'Lucas', '79074426050', 300);

insert into vacancies(id, code, status) values (10, 'A-01', 'BUSY');
insert into vacancies(id, code, status) values (20, 'A-02', 'BUSY');
insert into vacancies(id, code, status) values (30, 'A-03', 'BUSY');
insert into vacancies(id, code, status) values (40, 'A-04', 'BUSY');
insert into vacancies(id, code, status) values (50, 'A-05', 'BUSY');

insert into clients_have_vacancies(receipt_number, plate, brand, model, color, entry_date, client_id, vacancy_id)
values('20231209-174022', 'QWE-1489', 'FIAT', 'Palio v1', 'GREEN', '2023-03-13 10:15:00', 10, 10);
insert into clients_have_vacancies(receipt_number, plate, brand, model, color, entry_date, client_id, vacancy_id)
values('20231209-174023', 'QWE-7529', 'FIAT', 'Palio v2', 'BLUE', '2023-03-13 10:25:00', 20, 20);
insert into clients_have_vacancies(receipt_number, plate, brand, model, color, entry_date, client_id, vacancy_id)
values('20231209-174024', 'QWE-1159', 'FIAT', 'Palio v3', 'GRAY', '2023-03-13 10:35:00', 10, 30);
insert into clients_have_vacancies(receipt_number, plate, brand, model, color, entry_date, client_id, vacancy_id)
values('20231209-174025', 'QWE-7528', 'FIAT', 'Palio v4', 'BLUE', '2023-03-13 10:45:00', 20, 40);
insert into clients_have_vacancies(receipt_number, plate, brand, model, color, entry_date, client_id, vacancy_id)
values('20231209-174026', 'QWE-1154', 'FIAT', 'Palio v5', 'GRAY', '2023-03-13 10:55:00', 10, 50);