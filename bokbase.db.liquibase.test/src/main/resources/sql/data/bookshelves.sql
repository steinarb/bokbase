--liquibase formatted sql
--changeset sb:bookshelves
insert into bookshelves (account_id, bookshelf, book_id) values (1, 'read', 1);
insert into bookshelves (account_id, bookshelf, book_id) values (1, 'read', 2);
insert into bookshelves (account_id, bookshelf, book_id) values (1, 'to-read', 3);
insert into bookshelves (account_id, bookshelf, book_id) values (1, 'read', 4);
