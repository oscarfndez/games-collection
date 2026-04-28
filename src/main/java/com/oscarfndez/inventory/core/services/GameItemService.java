package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.GameItem;
import com.oscarfndez.inventory.ports.repositories.GameItemRepository;
import com.oscarfndez.inventory.ports.repositories.GameRepository;
import lombok.AllArgsConstructor;
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

    public GameItem addToCollection(UUID userId, UUID gameId, UUID platformId) {
        Game game = gameRepository.retrieveOne(gameId);
        boolean gameAvailableOnPlatform = game.getPlatforms().stream()
                .anyMatch(platform -> platform.getId().equals(platformId));

        if (!gameAvailableOnPlatform) {
            throw new IllegalArgumentException("Game is not available on selected platform.");
        }

        return gameItemRepository.save(UUID.randomUUID(), userId, gameId, platformId);
    }

    public void removeFromCollection(UUID id, UUID userId) {
        gameItemRepository.deleteOne(id, userId);
    }
}
