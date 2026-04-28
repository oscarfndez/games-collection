CREATE TABLE IF NOT EXISTS game_platform (
    game_id UUID NOT NULL REFERENCES game(id) ON DELETE CASCADE,
    platform_id UUID NOT NULL REFERENCES platform(id) ON DELETE CASCADE,
    PRIMARY KEY (game_id, platform_id)
);

INSERT INTO game_platform (game_id, platform_id)
SELECT id, platform_id
FROM game
WHERE platform_id IS NOT NULL
ON CONFLICT DO NOTHING;

ALTER TABLE game DROP COLUMN IF EXISTS platform_id;
