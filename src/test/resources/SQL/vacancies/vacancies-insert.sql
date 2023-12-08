insert into users (id, username, password, role) values (100, 'maria@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_ADMIN');
insert into users (id, username, password, role) values (200, 'joao@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (300, 'lucas@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (400, 'mateus@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');

insert into clients (id, name, cpf, user_id) values (10, 'Joao', '85212472067', 200);
insert into clients (id, name, cpf, user_id) values (20, 'Lucas', '79074426050', 300);

insert into vacancies(id, code, status) values (10, 'A-01', 'FREE');
insert into vacancies(id, code, status) values (20, 'A-02', 'FREE');
insert into vacancies(id, code, status) values (30, 'A-03', 'BUSY');
insert into vacancies(id, code, status) values (40, 'A-04', 'FREE');