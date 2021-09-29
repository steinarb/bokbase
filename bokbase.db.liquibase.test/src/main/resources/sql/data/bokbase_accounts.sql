--liquibase formatted sql
--changeset sb:bokbase_accounts
insert into bokbase_accounts (username) values ('jad');
