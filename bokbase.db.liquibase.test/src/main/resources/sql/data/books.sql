--liquibase formatted sql
--changeset sb:books
insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, published_date, isbn13) values ('Captain Vorpatril''s Alliance', 'A space romance', 1, 13.0, 1, 4, 1, 'Hardcover', 414, '2012-09-01', '9781451639155');
insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, published_date, isbn13) values ('Shards of Honor', 'A space romance', 1, 1.0, 1, 4, 1, 'Paperback', 253, '1986-06-01', '9780671655747');
insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, published_date, isbn13) values ('Citadel', null, 2, 3.0, 2, 4, 2, 'Hardcover', 333, '2021-08-10', '9781542027243');
insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, published_date, isbn13) values ('Juicy Ghosts', null, null, null, 3, 4, 3, 'Hardcover', 332, '2021-09-15','9781940948485');
