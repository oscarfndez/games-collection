package com.gamescollection.core.port;

import com.gamescollection.core.model.Game;

public interface GameService {

    Game createGame(String name, String description);
}
