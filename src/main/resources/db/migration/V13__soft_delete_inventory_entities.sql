ALTER TABLE game
    ADD COLUMN IF NOT EXISTS deleted boolean NOT NULL DEFAULT false;

ALTER TABLE platform
    ADD COLUMN IF NOT EXISTS deleted boolean NOT NULL DEFAULT false;

ALTER TABLE studio
    ADD COLUMN IF NOT EXISTS deleted boolean NOT NULL DEFAULT false;
