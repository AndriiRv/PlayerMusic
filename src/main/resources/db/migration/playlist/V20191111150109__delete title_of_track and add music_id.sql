ALTER TABLE playlist
    DROP COLUMN title_of_track,
    ADD COLUMN music_id INTEGER REFERENCES music (id);