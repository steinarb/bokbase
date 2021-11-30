--liquibase formatted sql
--changeset sb:series
insert into publishers (publisher_name) values ('Baen Books');
insert into publishers (publisher_name) values ('47 North');
insert into publishers (publisher_name) values ('Transreal Books');
