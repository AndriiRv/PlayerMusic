CREATE TABLE not_like
(
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    music_id  INTEGER NOT NULL REFERENCES music (id) ON DELETE CASCADE,
    is_hidden BOOLEAN
);
