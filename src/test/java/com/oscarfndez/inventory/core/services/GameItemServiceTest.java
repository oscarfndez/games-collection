package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.GameItem;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.ports.repositories.GameItemRepository;
import com.oscarfndez.inventory.ports.repositories.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameItemServiceTest {

    @Mock
    private GameItemRepository gameItemRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameItemService gameItemService;

    @Test
    void retrieveByUserIdDelegatesToRepository() {
        UUID userId = UUID.randomUUID();
        List<GameItem> collection = List.of(gameItem(userId, game(), platform()));
        when(gameItemRepository.findByUserId(userId)).thenReturn(collection);

        assertThat(gameItemService.retrieveByUserId(userId)).isSameAs(collection);
    }

    @Test
    void retrievePageBuildsPageableWithMappedSortAndTrimmedSearch() {
        UUID userId = UUID.randomUUID();
        Page<GameItem> page = new PageImpl<>(List.of(gameItem(userId, game(), platform())));
        when(gameItemRepository.searchByUserId(any(UUID.class), any(), any(Pageable.class))).thenReturn(page);

        assertThat(gameItemService.retrievePage(userId, " elden ", "platform", "desc", 1, 5)).isSameAs(page);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameItemRepository).searchByUserId(eq(userId), eq("elden"), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(5);
        Sort.Order order = pageable.getSort().getOrderFor("platform.name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void addToCollectionPersistsOwnershipWhenGameIsAvailableOnPlatform() {
        UUID userId = UUID.randomUUID();
        Platform platform = platform();
        Game game = game(platform);
        GameItem savedGameItem = gameItem(userId, game, platform);
        when(gameRepository.retrieveOne(game.getId())).thenReturn(game);
        when(gameItemRepository.save(any(UUID.class), any(UUID.class), any(UUID.class), any(UUID.class)))
                .thenReturn(savedGameItem);

        GameItem result = gameItemService.addToCollection(userId, game.getId(), platform.getId());

        assertThat(result).isSameAs(savedGameItem);
        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(gameItemRepository).save(idCaptor.capture(), eq(userId), eq(game.getId()), eq(platform.getId()));
        assertThat(idCaptor.getValue()).isNotNull();
    }

    @Test
    void addToCollectionRejectsPlatformThatDoesNotBelongToGame() {
        UUID userId = UUID.randomUUID();
        UUID unavailablePlatformId = UUID.randomUUID();
        Game game = game(platform());
        when(gameRepository.retrieveOne(game.getId())).thenReturn(game);

        assertThatThrownBy(() -> gameItemService.addToCollection(userId, game.getId(), unavailablePlatformId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Game is not available on selected platform.");

        verify(gameItemRepository, never()).save(any(), any(), any(), any());
    }

    @Test
    void removeFromCollectionDelegatesToRepository() {
        UUID gameItemId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        gameItemService.removeFromCollection(gameItemId, userId);

        verify(gameItemRepository).deleteOne(gameItemId, userId);
    }

    private static GameItem gameItem(UUID userId, Game game, Platform platform) {
        return GameItem.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .game(game)
                .platform(platform)
                .build();
    }

    private static Game game(Platform... platforms) {
        return Game.builder()
                .id(UUID.randomUUID())
                .name("Elden Ring")
                .description("Action RPG")
                .platforms(List.of(platforms))
                .imageUrl("elden-ring.png")
                .build();
    }

    private static Platform platform() {
        return Platform.builder()
                .id(UUID.randomUUID())
                .name("Steam")
                .description("PC")
                .imageUrl("steam.png")
                .build();
    }
}
