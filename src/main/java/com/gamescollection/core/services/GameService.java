package com.gamescollection.core.services;

import com.framework.architecture.hexagonal.persistence.HexagonalRepository;
import com.gamescollection.adapters.persistence.entities.GameEntity;
import com.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.gamescollection.adapters.rest.dtos.PlatformDto;
import com.gamescollection.core.model.Game;
import com.gamescollection.core.model.Platform;
import com.gamescollection.ports.repositories.PlatformRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class GameService {

    private final HexagonalRepository<Game, GameEntity> gameRepository;
    private final HexagonalRepository<Platform, PlatformEntity> platformRepository;

    public Game createGame(String name, String description, UUID platformId) {

        return gameRepository.save(new Game(UUID.randomUUID(), name, description, platformRepository.retrieveOne(platformId)));
    }
}
