CREATE TABLE message
(
    id      SERIAL PRIMARY KEY,
    chat_id INTEGER REFERENCES chat (id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES "user" (id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    date    DATE NOT NULL,
    time    TEXT NOT NULL
);
