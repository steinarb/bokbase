--liquibase formatted sql
--changeset sb:book_ratings
insert into book_ratings (account_id, book_id, rating, month_read, year_read) values (1, 1, 5, 7, 2013);
insert into book_ratings (account_id, book_id, rating, month_read, year_read) values (1, 2, 5, 9, 1988);
insert into book_ratings (account_id, book_id, rating, month_read, year_read) values (1, 3, null, null, null);
insert into book_ratings (account_id, book_id, rating, month_read, year_read) values (1, 4, 5, 10, 2021);
