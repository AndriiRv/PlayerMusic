ALTER TABLE chat_user
    DROP COLUMN user_id,
    ADD COLUMN user_id INTEGER REFERENCES "user" (id) ON DELETE CASCADE;