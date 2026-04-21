package com.oscarfndez.gamescollection.ports.repositories;

import com.oscarfndez.framework.adapters.persistence.mappers.HexagonalRepository;
import com.oscarfndez.gamescollection.adapters.persistence.entities.GameEntity;
import com.oscarfndez.gamescollection.core.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class GameRepository extends HexagonalRepository<Game, GameEntity> {

    @Autowired
    private GameJpaRepository gameJpaRepository;

    public List<Game> search(String search) {
        return gameJpaRepository.search(search)
                .stream()
                .map(modelEntityMapper::entityToModel)
                .toList();
    }
}