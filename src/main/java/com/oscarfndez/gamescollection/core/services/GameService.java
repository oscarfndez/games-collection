package com.oscarfndez.gamescollection.core.services;

import com.oscarfndez.framework.adapters.persistence.mappers.HexagonalRepository;
import com.oscarfndez.gamescollection.adapters.persistence.entities.GameEntity;
import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.core.model.Game;
import com.oscarfndez.gamescollection.core.model.Platform;
import com.oscarfndez.gamescollection.ports.repositories.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Component
public class GameService {

    private final GameRepository gameRepository;
    private final HexagonalRepository<Platform, PlatformEntity> platformRepository;

    public Game retrieveOne(UUID id) {
        return gameRepository.retrieveOne(id);
    }

    public List<Game> retrieveAny() {
        return gameRepository.retrieveAny();
    }

    public List<Game> retrieveAny(String search) {
        if (search == null || search.isBlank()) {
            return gameRepository.retrieveAny();
        }

        return gameRepository.search(search.trim());
    }

    public List<Game> retrieveAny(String search, String sortField, String sortDir) {

        String field = mapSortField(sortField);
        boolean asc = !"desc".equalsIgnoreCase(sortDir);

        if (search == null || search.isBlank()) {
            return gameRepository.findAllSorted(field, asc);
        }

        return gameRepository.searchAndSort(search.trim(), field, asc);
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


    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "name" -> "g.name";
            case "description" -> "g.description";
            case "platform" -> "p.name";
            default -> "g.name";
        };
    }
}
