CREATE TABLE message
(
    id      SERIAL PRIMARY KEY,
    chat_id INTEGER REFERENCES chat (id),
    user_id INTEGER REFERENCES "user" (id),
    message TEXT NOT NULL
);