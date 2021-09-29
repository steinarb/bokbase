--liquibase formatted sql
--changeset sb:series
insert into publishers (publisher_name) values ('Baen Books');
