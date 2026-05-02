package com.oscarfndez.inventory.adapters.rest.controllers;

import com.oscarfndez.inventory.adapters.rest.dtos.GameItemDto;
import com.oscarfndez.inventory.adapters.rest.dtos.mappers.GameItemModelDtoMapper;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.GameItem;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.core.services.GameItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameItemControllerTest {

    @Mock
    private GameItemService gameItemService;

    @Mock
    private GameItemModelDtoMapper gameItemModelDtoMapper;

    @InjectMocks
    private GameItemController gameItemController;

    @Test
    void loadCollectionUsesAuthenticatedUserIdFromJwt() {
        UUID userId = UUID.randomUUID();
        GameItem gameItem = gameItem(userId);
        GameItemDto dto = dto(gameItem);
        when(gameItemService.retrievePage(userId, "elden", "game", "asc", 0, 10))
                .thenReturn(new PageImpl<>(List.of(gameItem)));
        when(gameItemModelDtoMapper.mapToDTO(gameItem)).thenReturn(dto);

        var response = gameItemController.loadCollection("elden", "game", "asc", 0, 10, requestWithUserId(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).containsExactly(dto);
        assertThat(response.getBody().getPage()).isZero();
        assertThat(response.getBody().getSize()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
    }

    @Test
    void addToCollectionIgnoresUserIdFromBodyAndUsesAuthenticatedUserId() {
        UUID authenticatedUserId = UUID.randomUUID();
        UUID maliciousUserId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        UUID platformId = UUID.randomUUID();
        GameItemDto requestDto = GameItemDto.builder()
                .userId(maliciousUserId)
                .gameId(gameId)
                .platformId(platformId)
                .build();
        GameItem savedGameItem = gameItem(authenticatedUserId);
        GameItemDto responseDto = dto(savedGameItem);
        when(gameItemService.addToCollection(authenticatedUserId, gameId, platformId)).thenReturn(savedGameItem);
        when(gameItemModelDtoMapper.mapToDTO(savedGameItem)).thenReturn(responseDto);

        var response = gameItemController.addToCollection(requestDto, requestWithUserId(authenticatedUserId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(responseDto);
    }

    @Test
    void deleteFromCollectionUsesAuthenticatedUserId() {
        UUID userId = UUID.randomUUID();
        UUID gameItemId = UUID.randomUUID();

        var response = gameItemController.deleteFromCollection(gameItemId, requestWithUserId(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(gameItemService).removeFromCollection(gameItemId, userId);
    }

    private static MockHttpServletRequest requestWithUserId(UUID userId) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("authenticatedUserId", userId);
        return request;
    }

    private static GameItem gameItem(UUID userId) {
        Platform platform = Platform.builder()
                .id(UUID.randomUUID())
                .name("Steam")
                .description("PC")
                .imageUrl("steam.png")
                .build();
        Game game = Game.builder()
                .id(UUID.randomUUID())
                .name("Elden Ring")
                .description("Action RPG")
                .platforms(List.of(platform))
                .imageUrl("elden-ring.png")
                .build();

        return GameItem.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .game(game)
                .platform(platform)
                .active(true)
                .build();
    }

    private static GameItemDto dto(GameItem gameItem) {
        return GameItemDto.builder()
                .id(gameItem.getId())
                .userId(gameItem.getUserId())
                .gameId(gameItem.getGame().getId())
                .gameName(gameItem.getGame().getName())
                .gameImageUrl(gameItem.getGame().getImageUrl())
                .platformId(gameItem.getPlatform().getId())
                .platformName(gameItem.getPlatform().getName())
                .build();
    }
}
