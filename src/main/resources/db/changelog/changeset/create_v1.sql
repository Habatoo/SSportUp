create table users (
user_id  char(36),
first_name varchar(16),
last_name varchar(16),
second_name varchar(16),
birth_date timestamp,
login varchar(16),
password varchar(20),
telegram_chat_id char(36),
roles varchar(255),
primary key (user_id)
);