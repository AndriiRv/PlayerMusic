CREATE TABLE music
(
    id     SERIAL PRIMARY KEY,
    title  TEXT NOT NULL,
    singer TEXT,
    length TEXT,
    size   DOUBLE PRECISION,
    date   DATE
);