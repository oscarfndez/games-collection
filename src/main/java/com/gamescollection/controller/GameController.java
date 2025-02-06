package com.gamescollection.controller;

import com.gamescollection.core.model.Game;
import com.gamescollection.core.port.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/game")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public GameDTO createGame(GameDTO gameDTO) {
        return mapToDTO(gameService.createGame(gameDTO.name(), gameDTO.description()));
    }

    private GameDTO mapToDTO(Game game) {
        return new GameDTO(game.id(),  game.name(), game.description());
    }

}