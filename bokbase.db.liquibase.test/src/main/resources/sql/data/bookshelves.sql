--liquibase formatted sql
--changeset sb:bookshelves
insert into bookshelves (account_id, bookshelf, book_id) values (1, 'to-read', 1);
