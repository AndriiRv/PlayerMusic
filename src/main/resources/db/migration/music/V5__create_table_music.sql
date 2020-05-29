CREATE TABLE music
(
    id         SERIAL PRIMARY KEY,
    full_title TEXT,
    title      TEXT,
    singer     TEXT,
    album      TEXT,
    year       TEXT,
    genre      TEXT,
    length     TEXT,
    size       DOUBLE PRECISION,
    date_time  TIMESTAMP
);
