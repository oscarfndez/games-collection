CREATE TABLE IF NOT EXISTS studio (
    id UUID PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(2048) NOT NULL,
    location VARCHAR(256) NOT NULL,
    first_party BOOLEAN
);

ALTER TABLE game
ADD COLUMN IF NOT EXISTS studio_id UUID REFERENCES studio(id) ON DELETE SET NULL;

INSERT INTO studio (id, name, description, location, first_party)
VALUES
    ('923e4567-e89b-12d3-a456-426614174000', 'Santa Monica Studio', 'American video game studio known for the God of War series.', 'Los Angeles, California, United States', true),
    ('923e4567-e89b-12d3-a456-426614174001', 'Turn 10 Studios', 'American video game studio known for the Forza Motorsport series.', 'Redmond, Washington, United States', true),
    ('923e4567-e89b-12d3-a456-426614174002', 'PlatinumGames', 'Japanese video game studio known for action games such as Bayonetta and NieR:Automata.', 'Osaka, Japan', false)
ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    description = EXCLUDED.description,
    location = EXCLUDED.location,
    first_party = EXCLUDED.first_party;
