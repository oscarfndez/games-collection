package com.oscarfndez.gamescollection.core.services;

import com.oscarfndez.framework.adapters.persistence.HexagonalRepository;
import com.oscarfndez.gamescollection.adapters.persistence.entities.GameEntity;
import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.core.model.Game;
import com.oscarfndez.gamescollection.core.model.Platform;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Component
public class GameService {

    private final HexagonalRepository<Game, GameEntity> gameRepository;
    private final HexagonalRepository<Platform, PlatformEntity> platformRepository;

    public Game retrieveOne(UUID id) {
        return gameRepository.retrieveOne(id);
    }

    public List<Game> retrieveAny() {
        return gameRepository.retrieveAny();
    }

    public Game create(String name, String description, UUID platformId) {

        return gameRepository.save(
                Game.builder().id(UUID.randomUUID()).name(name).description(description).platform(platformRepository.retrieveOne(platformId)).build());
    }

    public Game updateGame(UUID id, String name, String description, UUID platformId) {
        return gameRepository.save(Game.builder().id(id).name(name).description(description).platform(platformRepository.retrieveOne(platformId)).build());
    }

    public void deleteOne(UUID id) {
        gameRepository.deleteOne(id);
    }
}
