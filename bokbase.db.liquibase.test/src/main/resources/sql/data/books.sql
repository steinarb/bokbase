--liquibase formatted sql
--changeset sb:books
insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, year_published) values ('Captain Vorpatril''s Alliance', 'A space romance', 1, 13.0, 1, 4, 1, 'Hardcover', 414, 2013);
