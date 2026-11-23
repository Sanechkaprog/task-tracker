CREATE TABLE users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(300) NOT NULL,

    CONSTRAINT users_pk PRIMARY KEY (user_id),
    CONSTRAINT username_uq UNIQUE (username)
)