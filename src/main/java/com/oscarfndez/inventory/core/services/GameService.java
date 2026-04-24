package com.oscarfndez.inventory.core.services;

import com.oscarfndez.framework.adapters.persistence.repositories.HexagonalRepository;
import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.ports.repositories.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Game> retrievePage(String search, String sortField, String sortDir, int page, int size) {
        String mappedSortField = mapSortField(sortField);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortField));

        return gameRepository.search(search == null ? "" : search.trim(), pageable);
    }

    public Game create(String name, String description, UUID platformId, String imageUrl) {
        return gameRepository.save(
                Game.builder()
                        .id(UUID.randomUUID())
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .platform(platformRepository.retrieveOne(platformId))
                        .build());
    }

    public Game updateGame(UUID id, String name, String description, UUID platformId, String imageUrl) {
        return gameRepository.save(
                Game.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .platform(platformRepository.retrieveOne(platformId))
                        .build());
    }

    public void deleteOne(UUID id) {
        gameRepository.deleteOne(id);
    }

    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "name" -> "name";
            case "description" -> "description";
            case "platform" -> "platform.name";
            default -> "name";
        };
    }
}
