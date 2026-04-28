package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.inventory.adapters.rest.dtos.GameDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.GameModelDtoMapper;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.core.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameModelDtoMapper gameModelDtoMapper;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Test
    void loadGameReturnsGameDto() {
        UUID gameId = UUID.randomUUID();
        Game game = game(gameId);
        GameDto dto = gameDto(gameId);
        when(gameService.retrieveOne(gameId)).thenReturn(game);
        when(gameModelDtoMapper.mapToDTO(game)).thenReturn(dto);

        var response = gameController.loadGame(gameId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(dto);
    }

    @Test
    void loadAllGamesReturnsPagedDtoResponse() {
        UUID gameId = UUID.randomUUID();
        Game game = game(gameId);
        GameDto dto = gameDto(gameId);
        when(gameService.retrievePage("zelda", "name", "asc", 0, 10))
                .thenReturn(new PageImpl<>(List.of(game)));
        when(gameModelDtoMapper.mapToDTO(game)).thenReturn(dto);

        var response = gameController.loadAllGames("zelda", "name", "asc", 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).containsExactly(dto);
        assertThat(response.getBody().getPage()).isZero();
        assertThat(response.getBody().getSize()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
    }

    @Test
    void createGameDelegatesToServiceAndReturnsCreatedDto() {
        UUID platformId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        GameDto request = GameDto.builder()
                .name("Zelda")
                .description("Adventure")
                .platformIds(List.of(platformId))
                .imageUrl("zelda.png")
                .build();
        Game createdGame = game(gameId);
        GameDto responseDto = gameDto(gameId);
        when(gameService.create("Zelda", "Adventure", List.of(platformId), "zelda.png")).thenReturn(createdGame);
        when(gameModelDtoMapper.mapToDTO(createdGame)).thenReturn(responseDto);

        var response = gameController.createGame(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(responseDto);
    }

    @Test
    void updateGameDelegatesToServiceAndReturnsOkDto() {
        UUID platformId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        GameDto request = GameDto.builder()
                .name("Zelda")
                .description("Adventure")
                .platformIds(List.of(platformId))
                .imageUrl("zelda.png")
                .build();
        Game updatedGame = game(gameId);
        GameDto responseDto = gameDto(gameId);
        when(gameService.updateGame(gameId, "Zelda", "Adventure", List.of(platformId), "zelda.png")).thenReturn(updatedGame);
        when(gameModelDtoMapper.mapToDTO(updatedGame)).thenReturn(responseDto);

        var response = gameController.updateGame(gameId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(responseDto);
    }

    @Test
    void deleteGameDelegatesToServiceAndReturnsNoContent() {
        UUID gameId = UUID.randomUUID();

        var response = gameController.deleteGame(gameId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(gameService).deleteOne(gameId);
    }

    private static Game game(UUID gameId) {
        Platform platform = Platform.builder()
                .id(UUID.randomUUID())
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
        return Game.builder()
                .id(gameId)
                .name("Zelda")
                .description("Adventure")
                .platforms(List.of(platform))
                .imageUrl("zelda.png")
                .build();
    }

    private static GameDto gameDto(UUID gameId) {
        return GameDto.builder()
                .id(gameId)
                .name("Zelda")
                .description("Adventure")
                .platformIds(List.of(UUID.randomUUID()))
                .platformName("Nintendo Switch")
                .imageUrl("zelda.png")
                .build();
    }
}
