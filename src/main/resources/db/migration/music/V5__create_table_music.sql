CREATE TABLE music
(
    id         SERIAL PRIMARY KEY,
    title      TEXT,
    singer     TEXT,
    full_title TEXT,
    length     TEXT,
    size       DOUBLE PRECISION,
    date       DATE,
    time       TEXT,
    date_time  TEXT
);
