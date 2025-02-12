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