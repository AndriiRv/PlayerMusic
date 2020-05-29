CREATE TABLE track_cover
(
    id       SERIAL PRIMARY KEY,
    music_id INTEGER UNIQUE NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    picture  BYTEA
);