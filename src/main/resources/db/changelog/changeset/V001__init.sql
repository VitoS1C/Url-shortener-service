CREATE TABLE IF NOT EXISTS url
(
    hash       VARCHAR(6) PRIMARY KEY,
    url        VARCHAR(255) NOT NULL UNIQUE,
    created_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hash
(
    hash VARCHAR(6) PRIMARY KEY
);

CREATE SEQUENCE unique_hash_number_seq START WITH 1 INCREMENT BY 1;