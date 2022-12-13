
CREATE SEQUENCE user_id_seq
  	START WITH 1
  	INCREMENT BY 1
  	NO MINVALUE
  	MAXVALUE 2147483647;

CREATE TABLE users(
	id BIGINT NOT NULL DEFAULT nextval('user_id_seq'),
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(100) NOT NULL,
	balance NUMERIC NOT NULL,
	should_notify_about_under_limit_balance BOOLEAN NOT NULL,
	PRIMARY KEY(id)
);

-- USERS

-- password: 1234
INSERT INTO users(id, email, password, balance, should_notify_about_under_limit_balance)
VALUES (1, 'test_email_1', '$2a$10$CXvP2KtsVnFUnmeT1SC3OerHdY0J6CfF0l/8eS5jeYvVw8gud7Gte', 1500, true);