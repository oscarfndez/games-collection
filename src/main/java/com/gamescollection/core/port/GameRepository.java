package com.gamescollection.core.port;

import com.gamescollection.core.model.Game;

public interface GameRepository {

    Game save(Game game);
}
