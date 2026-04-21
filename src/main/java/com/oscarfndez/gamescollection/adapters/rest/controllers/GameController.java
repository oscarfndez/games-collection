package com.oscarfndez.gamescollection.adapters.rest.controllers;

import com.oscarfndez.framework.core.model.dto.PageResponseDto;
import com.oscarfndez.gamescollection.adapters.rest.dtos.GameDto;
import com.oscarfndez.gamescollection.adapters.rest.dtos.mappers.GameModelDtoMapper;
import com.oscarfndez.gamescollection.core.model.Game;
import com.oscarfndez.gamescollection.core.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@AllArgsConstructor
@PreAuthorize("@authorizationService.hasRole('USER')")
public class GameController {

    private final GameModelDtoMapper gameModelDtoMapper;
    private final GameService gameService;


    @GetMapping("/api/game")
    public ResponseEntity<GameDto> loadGame(@RequestParam final UUID id) {
        return new ResponseEntity<>(
                gameModelDtoMapper.mapToDTO(gameService.retrieveOne(id))
                , HttpStatus.OK);
    }

    @GetMapping("/api/game/all")
    public ResponseEntity<PageResponseDto<GameDto>> loadAllGames(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false, defaultValue = "name") final String sortField,
            @RequestParam(required = false, defaultValue = "asc") final String sortDir,
            @RequestParam(required = false, defaultValue = "0") final int page,
            @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        Page<Game> resultPage = gameService.retrievePage(search, sortField, sortDir, page, size);

        List<GameDto> content = resultPage.getContent()
                .stream()
                .map(gameModelDtoMapper::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new PageResponseDto<>(
                        content,
                        resultPage.getNumber(),
                        resultPage.getSize(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages()
                )
        );
    }

    @PostMapping("/api/game")
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto) {

        return new ResponseEntity<>(
                gameModelDtoMapper.mapToDTO(gameService.create(gameDto.getName(), gameDto.getDescription(), gameDto.getPlatformId())
                ), HttpStatus.CREATED);
    }

    @PutMapping("/api/game")
    public ResponseEntity<GameDto> updateGame(@RequestParam final UUID id, @RequestBody GameDto gameDto) {
        return new ResponseEntity<>(
                gameModelDtoMapper.mapToDTO(gameService.updateGame(id, gameDto.getName(), gameDto.getDescription(), gameDto.getPlatformId())
                ), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/game")
    public ResponseEntity<Void> deleteGame(@RequestParam final UUID id) {
        gameService.deleteOne(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
