package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.core.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameEntityModelMapper {


    private final PlatformEntityModelMapper platformEntityModelMapper;


    public GameEntity modelToEntity(Game game) {
        return new GameEntity(
                game.getId(),
                game.getName(),
                game.getDescription(),
                game.getImageUrl(),
                game.getPlatforms().stream()
                        .map(platformEntityModelMapper::modelToEntity)
                        .toList()
        );
    }

    public Game entityToModel(GameEntity gameEntity) {
        return new Game(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getDescription(),
                gameEntity.getPlatforms().stream()
                        .map(platformEntityModelMapper::entityToModel)
                        .toList(),
                gameEntity.getImageUrl()
        );
    }
}
