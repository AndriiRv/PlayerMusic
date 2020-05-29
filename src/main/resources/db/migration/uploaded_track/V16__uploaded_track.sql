CREATE TABLE uploaded_track
(
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER NOT NULL REFERENCES "user" (id),
    music_id  INTEGER UNIQUE NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    date_time TIMESTAMP NOT NULL
);