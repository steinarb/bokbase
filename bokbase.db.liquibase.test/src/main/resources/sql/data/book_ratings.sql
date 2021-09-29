--liquibase formatted sql
--changeset sb:book_ratings
insert into book_ratings (account_id, book_id, rating) values (1, 1, 5);
