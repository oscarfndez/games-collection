CREATE TABLE IF NOT EXISTS game_item (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    game_id UUID NOT NULL REFERENCES game(id) ON DELETE CASCADE,
    platform_id UUID NOT NULL REFERENCES platform(id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_game_item_user_game_platform UNIQUE (user_id, game_id, platform_id)
);

