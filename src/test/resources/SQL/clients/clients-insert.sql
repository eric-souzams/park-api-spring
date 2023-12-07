insert into users (id, username, password, role) values (100, 'maria@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_ADMIN');
insert into users (id, username, password, role) values (200, 'joao@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (300, 'lucas@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');
insert into users (id, username, password, role) values (400, 'mateus@poc.dev', '$2a$10$ezozZgbQ4/w55B2dBdCQ6u.5qZbwxDZGJCpPGx/QQ27/y.SXyyGtu', 'ROLE_CLIENT');

insert into clients (id, name, cpf, user_id) values (10, 'Maira', '85212472067', 300);
insert into clients (id, name, cpf, user_id) values (20, 'Joao', '79074426050', 400);