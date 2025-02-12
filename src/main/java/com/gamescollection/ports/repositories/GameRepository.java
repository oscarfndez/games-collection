package com.gamescollection.ports.repositories;

import com.framework.architecture.hexagonal.persistence.HexagonalRepository;
import com.gamescollection.adapters.persistence.entities.GameEntity;
import com.gamescollection.core.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameRepository extends HexagonalRepository<Game, GameEntity> {

}
