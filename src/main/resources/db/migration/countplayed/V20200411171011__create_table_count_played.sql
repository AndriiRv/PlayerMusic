CREATE TABLE count_played
(
    id       SERIAL PRIMARY KEY,
    music_id INTEGER NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    counter  INTEGER
);