CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS _user (

    id uuid PRIMARY KEY DEFAULT uuid_generate_v4 (),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    status VARCHAR(20)
);


CREATE TABLE platform (
                          id UUID PRIMARY KEY,
                          name VARCHAR(256) NOT NULL,
                          description VARCHAR(2048) NOT NULL
);

CREATE TABLE game (
                      id UUID PRIMARY KEY,
                      name VARCHAR(256) NOT NULL,
                      description VARCHAR(2048) NOT NULL,
                      platform_id UUID REFERENCES platform(id) NOT NULL
);