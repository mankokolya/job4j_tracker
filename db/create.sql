CREATE DATABASE tracker;
\c tracker

create table items (
    id bigserial primary key,
    name text
);