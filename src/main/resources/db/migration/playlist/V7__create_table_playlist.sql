CREATE TABLE playlist
(
    id               SERIAL PRIMARY KEY,
    user_id          INTEGER REFERENCES "user" (id) ON DELETE CASCADE,
    title            TEXT NOT NULL
);
