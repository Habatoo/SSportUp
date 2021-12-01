create table users (
user_id  char(36),
first_name varchar(16),
last_name varchar(16),
second_name varchar(16),
birth_date timestamp,
login varchar(16),
password varchar(20),
telegram_chat_id char(36),
roles clob,
primary key (user_id)
);
--            <column name="user_id" type="char(36)"/>
--            <column name="first_name" type="varchar(16)"/>
--            <column name="last_name" type="varchar(32)"/>
--            <column name="second_name" type="varchar(32)"/>
--			<column name="birth_date" type="date"/>
--            <column name="login" type="varchar(16)"/>
--			<column name="password" type="varchar(20)"/>
--            <column name="telegram_chat_id" type="char(36)"/>
--			<column name="roles" type="clob"/>
