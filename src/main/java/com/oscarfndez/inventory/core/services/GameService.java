package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.ports.repositories.GameRepository;
import com.oscarfndez.inventory.ports.repositories.PlatformRepository;
import com.oscarfndez.inventory.ports.repositories.StudioRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final PlatformRepository platformRepository;
    private final StudioRepository studioRepository;

    @Cacheable(cacheNames = "games", key = "#p0")
    public Game retrieveOne(UUID id) {
        return gameRepository.retrieveOne(id);
    }

    @Cacheable(cacheNames = "gamePages", key = "'all'")
    public List<Game> retrieveAny() {
        return gameRepository.retrieveAny();
    }

    @Cacheable(cacheNames = "gamePages", key = "{#p0 == null ? '' : #p0.trim()}")
    public List<Game> retrieveAny(String search) {
        if (search == null || search.isBlank()) {
            return gameRepository.retrieveAny();
        }

        return gameRepository.search(search.trim());
    }

    @Cacheable(cacheNames = "gamePages", key = "{#p0 == null ? '' : #p0.trim(), #p1, #p2}")
    public List<Game> retrieveAny(String search, String sortField, String sortDir) {

        String field = mapSortField(sortField);
        boolean asc = !"desc".equalsIgnoreCase(sortDir);

        if (search == null || search.isBlank()) {
            return gameRepository.findAllSorted(field, asc);
        }

        return gameRepository.searchAndSort(search.trim(), field, asc);
    }

    @Cacheable(cacheNames = "gamePages", key = "{#p0 == null ? '' : #p0.trim(), #p1, #p2, #p3, #p4}")
    public Page<Game> retrievePage(String search, String sortField, String sortDir, int page, int size) {
        String mappedSortField = mapSortField(sortField);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mappedSortField));

        return gameRepository.search(search == null ? "" : search.trim(), pageable);
    }

    public Game create(String name, String description, UUID platformId, String imageUrl) {
        return create(name, description, List.of(platformId), imageUrl);
    }

    public Game create(String name, String description, List<UUID> platformIds, String imageUrl) {
        return create(name, description, platformIds, imageUrl, null);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "gamePages", allEntries = true),
            @CacheEvict(cacheNames = "collectionPages", allEntries = true)
    })
    public Game create(String name, String description, List<UUID> platformIds, String imageUrl, UUID studioId) {
        return gameRepository.save(
                Game.builder()
                        .id(UUID.randomUUID())
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .platforms(platformRepository.retrieveMany(platformIds))
                        .studio(studioId == null ? null : studioRepository.retrieveOne(studioId))
                        .build());
    }

    public Game updateGame(UUID id, String name, String description, UUID platformId, String imageUrl) {
        return updateGame(id, name, description, List.of(platformId), imageUrl);
    }

    public Game updateGame(UUID id, String name, String description, List<UUID> platformIds, String imageUrl) {
        return updateGame(id, name, description, platformIds, imageUrl, null);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "games", key = "#p0"),
            @CacheEvict(cacheNames = "gamePages", allEntries = true),
            @CacheEvict(cacheNames = "collectionPages", allEntries = true)
    })
    public Game updateGame(UUID id, String name, String description, List<UUID> platformIds, String imageUrl, UUID studioId) {
        return gameRepository.save(
                Game.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .platforms(platformRepository.retrieveMany(platformIds))
                        .studio(studioId == null ? null : studioRepository.retrieveOne(studioId))
                        .build());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "games", key = "#p0"),
            @CacheEvict(cacheNames = "gamePages", allEntries = true),
            @CacheEvict(cacheNames = "collectionPages", allEntries = true)
    })
    public void deleteOne(UUID id) {
        gameRepository.deleteOne(id);
    }

    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "name" -> "name";
            case "description" -> "description";
            case "platform" -> "platforms.name";
            default -> "name";
        };
    }
}
