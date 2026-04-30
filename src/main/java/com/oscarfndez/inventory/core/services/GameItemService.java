package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.GameItem;
import com.oscarfndez.inventory.ports.repositories.GameItemRepository;
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
public class GameItemService {

    private final GameItemRepository gameItemRepository;
    private final GameRepository gameRepository;

    public List<GameItem> retrieveByUserId(UUID userId) {
        return gameItemRepository.findByUserId(userId);
    }

    public Page<GameItem> retrievePage(UUID userId, String search, String sortField, String sortDir, int page, int size) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mapSortField(sortField)));
        return gameItemRepository.searchByUserId(userId, normalizeSearch(search), pageable);
    }

    public GameItem addToCollection(UUID userId, UUID gameId, UUID platformId) {
        validateGameAvailableOnPlatform(gameId, platformId);
        return gameItemRepository.save(UUID.randomUUID(), userId, gameId, platformId);
    }

    public GameItem updateCollectionItem(UUID id, UUID userId, UUID gameId, UUID platformId) {
        if (!gameItemRepository.existsByIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("Collection item not found.");
        }

        validateGameAvailableOnPlatform(gameId, platformId);
        return gameItemRepository.save(id, userId, gameId, platformId);
    }

    private void validateGameAvailableOnPlatform(UUID gameId, UUID platformId) {
        Game game = gameRepository.retrieveOne(gameId);
        boolean gameAvailableOnPlatform = game.getPlatforms().stream()
                .anyMatch(platform -> platform.getId().equals(platformId));

        if (!gameAvailableOnPlatform) {
            throw new IllegalArgumentException("Game is not available on selected platform.");
        }
    }

    public void removeFromCollection(UUID id, UUID userId) {
        gameItemRepository.deleteOne(id, userId);
    }

    private String normalizeSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        return search.trim();
    }

    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "platform" -> "platform.name";
            default -> "game.name";
        };
    }
}
