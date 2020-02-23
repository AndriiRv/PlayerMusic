CREATE TABLE chat_user
(
    user_id INTEGER NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    chat_id INTEGER NOT NULL REFERENCES chat (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, chat_id)
);
