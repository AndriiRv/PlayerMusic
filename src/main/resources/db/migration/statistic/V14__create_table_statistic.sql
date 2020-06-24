CREATE TABLE statistic
(
    id                SERIAL PRIMARY KEY,
    music_id          INTEGER UNIQUE NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    counter_played    INTEGER,
    counter_favourite INTEGER
);