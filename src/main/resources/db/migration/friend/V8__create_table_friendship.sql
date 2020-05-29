CREATE TABLE friendship
(
    first_friend             INTEGER NOT NULL REFERENCES "user" (id),
    second_friend            INTEGER NOT NULL REFERENCES "user" (id),
    date_of_start_friendship DATE NOT NULL,
    PRIMARY KEY (first_friend, second_friend)
);