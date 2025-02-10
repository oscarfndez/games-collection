package com.gamescollection.adapter.persistence;

import com.gamescollection.core.model.Game;
import com.gamescollection.core.port.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameDBRepository implements GameRepository {

    private final GameJPARepository gameJPARepository;

    @Override
    public Game save(Game game) {
        return entityToModel(gameJPARepository.save(modelToEntity(game)));
    }

    public GameEntity modelToEntity(Game game) {
        return new GameEntity(game.getId(), game.getName(), game.getDescription());
    }

    public Game entityToModel(GameEntity gameEntity) {
        return new Game(gameEntity.getId(), gameEntity.getName(), gameEntity.getDescription());
    }
}
