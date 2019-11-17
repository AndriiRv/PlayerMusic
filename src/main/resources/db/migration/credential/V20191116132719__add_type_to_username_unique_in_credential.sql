ALTER TABLE credential
    DROP COLUMN username,
    ADD COLUMN username TEXT NOT NULL UNIQUE;