CREATE TABLE user_playlist
(
    id               SERIAL PRIMARY KEY,
    user_playlist_id INTEGER NOT NULL REFERENCES playlist (id) ON DELETE CASCADE,
    music_id         INTEGER NOT NULL REFERENCES music (id) ON DELETE CASCADE
);
