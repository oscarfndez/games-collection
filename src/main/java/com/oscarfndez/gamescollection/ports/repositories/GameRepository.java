package com.oscarfndez.gamescollection.ports.repositories;

import com.oscarfndez.framework.adapters.persistence.HexagonalRepository;
import com.oscarfndez.gamescollection.adapters.persistence.entities.GameEntity;
import com.oscarfndez.gamescollection.core.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameRepository extends HexagonalRepository<Game, GameEntity> {

}
