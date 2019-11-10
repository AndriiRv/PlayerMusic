CREATE TABLE chat_user
(
    user_id INTEGER NOT NULL REFERENCES "user" (id),
    chat_id INTEGER NOT NULL REFERENCES chat (id),
    PRIMARY KEY (user_id, chat_id)
);