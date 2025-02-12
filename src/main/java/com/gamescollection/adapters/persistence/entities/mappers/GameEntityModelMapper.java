package com.gamescollection.adapters.persistence.entities.mappers;

import com.framework.architecture.hexagonal.persistence.ModelEntityMapper;
import com.gamescollection.adapters.persistence.entities.GameEntity;
import com.gamescollection.core.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameEntityModelMapper implements ModelEntityMapper<Game, GameEntity> {


    private final PlatformEntityModelMapper platformEntityModelMapper;


    public GameEntity modelToEntity(Game game) {
        return new GameEntity(game.getId(), game.getName(), game.getDescription(), platformEntityModelMapper.modelToEntity(game.getPlatform()));
    }

    public Game entityToModel(GameEntity gameEntity) {
        return new Game(gameEntity.getId(), gameEntity.getName(), gameEntity.getDescription(), platformEntityModelMapper.entityToModel(gameEntity.getPlatform()));
    }
}