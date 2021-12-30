--liquibase formatted sql
--changeset sb:book_ratings
insert into book_ratings (account_id, book_id, rating, finished_read_date) values (1, 1, 5, '2013-07-01');
insert into book_ratings (account_id, book_id, rating, finished_read_date) values (1, 2, 5, '1988-09-01');
insert into book_ratings (account_id, book_id, rating, finished_read_date) values (1, 3, null, null);
insert into book_ratings (account_id, book_id, rating, finished_read_date) values (1, 4, 5, '2021-12-05');
