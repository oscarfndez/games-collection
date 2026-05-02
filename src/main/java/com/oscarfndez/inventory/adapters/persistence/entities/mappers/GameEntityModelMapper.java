package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.StudioEntity;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
                game.getStudio() == null ? null : new StudioEntity(
                        game.getStudio().getId(),
                        game.getStudio().getName(),
                        game.getStudio().getDescription(),
                        game.getStudio().getLocation(),
                        game.getStudio().getFirstParty(),
                        new ArrayList<>()
                ),
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
                gameEntity.getImageUrl(),
                gameEntity.getStudio() == null ? null : Studio.builder()
                        .id(gameEntity.getStudio().getId())
                        .name(gameEntity.getStudio().getName())
                        .description(gameEntity.getStudio().getDescription())
                        .location(gameEntity.getStudio().getLocation())
                        .firstParty(gameEntity.getStudio().getFirstParty())
                        .games(List.of())
                        .build()
        );
    }
}
