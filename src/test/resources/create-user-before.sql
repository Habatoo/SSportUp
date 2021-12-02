DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users(
user_id  char(36),
first_name varchar(16),
last_name varchar(16),
second_name varchar(16),
birth_date timestamp,
login varchar(16),
password varchar(20),
telegram_chat_id char(36),
roles varchar(255));

INSERT INTO users
VALUES (
'lllll-lllll',
 'admin',
 'adminovich',
 'admin@a.com',
 parsedatetime('17-09-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),
 'admin1',
 '12345',
 't-admin',
 'admin, writer'
 ), (
'kkkkk-kkkkk',
'teacher',
'teacherovich',
'teacher@a.com',
parsedatetime('17-10-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),
'teacher1',
'12345',
't-teacher',
'writer'
), (
'mmmmm-mmmmm',
'user',
'userovich',
'user@a.com',
parsedatetime('17-12-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),
'user1',
'12345',
't-user',
'user'
);
