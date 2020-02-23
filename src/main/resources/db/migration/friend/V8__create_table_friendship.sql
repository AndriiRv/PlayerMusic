CREATE TABLE friendship
(
    first_friend  INTEGER NOT NULL REFERENCES "user" (id),
    second_friend INTEGER NOT NULL REFERENCES "user" (id),
    PRIMARY KEY (first_friend, second_friend)
);