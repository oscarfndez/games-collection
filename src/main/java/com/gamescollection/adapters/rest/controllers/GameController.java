package com.gamescollection.adapters.rest.controllers;

import com.gamescollection.adapters.rest.dtos.GameDto;
import com.gamescollection.adapters.rest.dtos.mappers.GameModelDtoMapper;
import com.gamescollection.core.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@AllArgsConstructor
public class GameController {

    private final GameModelDtoMapper gameModelDtoMapper;
    private final GameService gameService;

    @PostMapping
    public GameDto createGame(GameDto gameDto) {
        return gameModelDtoMapper.mapToDTO(gameService.createGame(gameDto.getName(), gameDto.getDescription(), gameDto.getPlatformId()));
    }
}
