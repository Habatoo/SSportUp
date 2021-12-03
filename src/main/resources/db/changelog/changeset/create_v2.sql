drop table if exists users cascade;

CREATE TABLE users (
user_id serial not null,
user_name varchar(255),
user_password varchar(255),
user_email varchar(255),
telegram_chat_id varchar(255),
user_creation_date timestamp,
last_visited_date timestamp,
roles varchar(255),
primary key (user_id)
);