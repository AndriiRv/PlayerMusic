CREATE TABLE lyric
(
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER REFERENCES "user" (id),
    music_id  INTEGER UNIQUE NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    text      TEXT NOT NULL,
    date_time TIMESTAMP NOT NULL
);