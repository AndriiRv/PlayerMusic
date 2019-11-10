CREATE TABLE "user"
(
    id            SERIAL PRIMARY KEY,
    credential_id INTEGER UNIQUE REFERENCES credential (id),
    surname       TEXT    NOT NULL,
    name          TEXT    NOT NULL,
    role_id       INTEGER NOT NULL REFERENCES role (id)
);