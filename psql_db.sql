create database company;

\connect company

create table employees (
    id serial primary key,
    name text not null,
    role text not null,
    salary decimal not null,
    added_date date not null
);

-- Example data, modify as desired
insert into employees values
(default, 'Erick Salinas', 'CEO', 3000.00, '2026-03-06'),
(default, 'Robert Smith', 'CIO', 2999.00, '2026-03-06'),
(default, 'Gilbert Wells', 'Project Manager', 1945.46, '2026-03-06'),
(default, 'Amy Easton', 'Secretary', 1510.22, '2026-03-06'),
(default, 'Cassandra Turing', 'Programmer', 1788.20, '2026-03-06'),
(default, 'Richard Duan', 'Accountant', 1789.67, '2026-03-06'),
(default, 'Elly Simpson', 'Human Resources', 1761.00, '2026-03-06'),
(default, 'Joanne Amos', 'Programmer', 1788.20, '2026-03-06'),
(default, 'Emmanuel Franco', 'QA', 1717.80, '2026-03-06'),
(default, 'Toby Harris', 'DBA', 1822.00, '2026-03-06'),

(default, 'Elena Vázquez', 'Social media director', 1400.00, '2026-03-06');
