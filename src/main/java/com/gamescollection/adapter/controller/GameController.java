package com.gamescollection.adapter.controller;

import com.gamescollection.core.model.Game;
import com.gamescollection.core.port.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public GameDTO createGame(GameDTO gameDTO) {
        return mapToDTO(gameService.createGame(gameDTO.getName(), gameDTO.getDescription()));
    }

    private GameDTO mapToDTO(Game game) {
        return GameDTO.builder().id(game.getId())
                .name(game.getName())
                .description(game.getDescription())
                .build();
    }

}
