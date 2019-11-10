CREATE TABLE playlist
(
    id             SERIAL PRIMARY KEY,
    user_id        INTEGER REFERENCES "user" (id),
    title          TEXT NOT NULL,
    title_of_track TEXT,
    is_hidden      BOOLEAN
);