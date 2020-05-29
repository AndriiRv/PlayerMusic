CREATE TABLE message
(
    id           SERIAL PRIMARY KEY,
    chat_id      INTEGER NOT NULL REFERENCES chat (id) ON DELETE CASCADE,
    user_id      INTEGER NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    message_text TEXT NOT NULL,
    date_time    TIMESTAMP NOT NULL
);
