CREATE TABLE credential
(
    id       SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    password TEXT NOT NULL
);