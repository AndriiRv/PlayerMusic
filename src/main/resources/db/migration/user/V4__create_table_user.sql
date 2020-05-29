CREATE TABLE "user"
(
    id                   SERIAL PRIMARY KEY,
    credential_id        INTEGER NOT NULL UNIQUE REFERENCES credential (id) ON DELETE CASCADE,
    surname              TEXT    NOT NULL,
    name                 TEXT    NOT NULL,
    role_id              INTEGER NOT NULL REFERENCES role (id),
    email                TEXT    NOT NULL UNIQUE,
    date_of_registration TIMESTAMP NOT NULL
);