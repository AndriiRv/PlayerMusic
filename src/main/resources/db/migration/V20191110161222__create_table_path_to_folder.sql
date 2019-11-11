CREATE TABLE path_to_folder
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES "user" (id) ON DELETE CASCADE,
    path    TEXT NOT NULL
);