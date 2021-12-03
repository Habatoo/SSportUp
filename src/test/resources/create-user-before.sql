DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users (
user_id bigint auto_increment,
user_name varchar(255),
user_password varchar(255),
user_email varchar(255),
telegram_chat_id varchar(255),
user_creation_date timestamp,
last_visited_date timestamp,
roles varchar(255),
primary key (user_id)
);

INSERT INTO users (
user_name, user_password, user_email, telegram_chat_id, user_creation_date, last_visited_date, roles
)
VALUES (
 'administrator',
 '$2a$10$7JGsM41kbXX7/vJ2lc3pb.wdoIoANWTme.NErCU2TSv1RcPnDaBaS',
 'admin@a.com',
  't-admin',
  {ts '2012-10-17 18:47:52.69'},
  CURRENT_TIMESTAMP(),
 'admin, writer'
 ), (
'moderator',
 '$2a$10$7JGsM41kbXX7/vJ2lc3pb.wdoIoANWTme.NErCU2TSv1RcPnDaBaS',
'moderator@a.com',
't-moderator',
{ts '2012-11-17 18:47:52.69'},
CURRENT_TIMESTAMP(),
'moderator, writer'
), (
'user',
 '$2a$10$7JGsM41kbXX7/vJ2lc3pb.wdoIoANWTme.NErCU2TSv1RcPnDaBaS',
'user@a.com',
't-user',
{ts '2012-12-17 18:47:52.69'},
CURRENT_TIMESTAMP(),
'user, writer'
);
