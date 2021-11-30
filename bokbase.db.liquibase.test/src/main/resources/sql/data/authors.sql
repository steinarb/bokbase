--liquibase formatted sql
--changeset sb:authors
insert into authors (author_firstname, author_lastname) values ('Lois McMaster', 'Bujold');
insert into authors (author_firstname, author_lastname) values ('Marko', 'Kloos');
insert into authors (author_firstname, author_lastname) values ('Rudy', 'Rucker');
