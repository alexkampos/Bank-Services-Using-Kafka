
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