package com.gamescollection.core.service;

import com.gamescollection.core.model.Game;
import com.gamescollection.core.port.GameRepository;
import com.gamescollection.core.port.GameService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Game createGame(String name, String description) {
        return gameRepository.save(new Game(UUID.randomUUID(), name, description));
    }
}
