CREATE TABLE favourite
(
    id                   SERIAL PRIMARY KEY,
    user_id              INTEGER NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    music_id             INTEGER NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    own_user_music_title TEXT,
    date                 TIMESTAMP
);
