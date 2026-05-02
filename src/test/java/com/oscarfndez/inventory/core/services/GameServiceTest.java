package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.ports.repositories.GameRepository;
import com.oscarfndez.inventory.ports.repositories.PlatformRepository;
import com.oscarfndez.inventory.ports.repositories.StudioRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlatformRepository platformRepository;

    @Mock
    private StudioRepository studioRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void retrieveAnyWithoutSearchDelegatesToRetrieveAny() {
        List<Game> games = List.of(game("Zelda", platform()));
        when(gameRepository.retrieveAny()).thenReturn(games);

        assertThat(gameService.retrieveAny("  ")).isSameAs(games);
    }

    @Test
    void retrieveAnyWithSearchTrimsAndDelegatesToSearch() {
        List<Game> games = List.of(game("Zelda", platform()));
        when(gameRepository.search("zelda")).thenReturn(games);

        assertThat(gameService.retrieveAny("  zelda  ")).isSameAs(games);
    }

    @Test
    void retrieveAnyWithSortMapsPlatformFieldAndDescendingDirection() {
        List<Game> games = List.of(game("Zelda", platform()));
        when(gameRepository.searchAndSort("nintendo", "platforms.name", false)).thenReturn(games);

        assertThat(gameService.retrieveAny(" nintendo ", "platform", "desc")).isSameAs(games);
    }

    @Test
    void retrievePageBuildsPageableWithMappedSortAndTrimmedSearch() {
        Page<Game> page = new PageImpl<>(List.of(game("Zelda", platform())));
        when(gameRepository.search(any(String.class), any(Pageable.class))).thenReturn(page);

        assertThat(gameService.retrievePage(" zelda ", "platform", "desc", 2, 25)).isSameAs(page);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(gameRepository).search(eq("zelda"), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(25);
        Sort.Order order = pageable.getSort().getOrderFor("platforms.name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void createBuildsGameWithPlatformAndPersistsIt() {
        UUID platformId = UUID.randomUUID();
        Platform platform = platform();
        when(platformRepository.retrieveMany(List.of(platformId))).thenReturn(List.of(platform));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game createdGame = gameService.create("Zelda", "Adventure", platformId, "zelda.png");

        assertThat(createdGame.getId()).isNotNull();
        assertThat(createdGame.getName()).isEqualTo("Zelda");
        assertThat(createdGame.getDescription()).isEqualTo("Adventure");
        assertThat(createdGame.getImageUrl()).isEqualTo("zelda.png");
        assertThat(createdGame.getPlatforms()).containsExactly(platform);
    }

    @Test
    void updateGameKeepsProvidedGameId() {
        UUID gameId = UUID.randomUUID();
        UUID platformId = UUID.randomUUID();
        Platform platform = platform();
        when(platformRepository.retrieveMany(List.of(platformId))).thenReturn(List.of(platform));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game updatedGame = gameService.updateGame(gameId, "Mario", "Platformer", platformId, "mario.png");

        assertThat(updatedGame.getId()).isEqualTo(gameId);
        assertThat(updatedGame.getName()).isEqualTo("Mario");
        assertThat(updatedGame.getPlatforms()).containsExactly(platform);
    }

    @Test
    void deleteOneDelegatesToRepository() {
        UUID gameId = UUID.randomUUID();

        gameService.deleteOne(gameId);

        verify(gameRepository).deleteOne(gameId);
    }

    private static Game game(String name, Platform platform) {
        return Game.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description("Description")
                .platforms(List.of(platform))
                .imageUrl("image.png")
                .build();
    }

    private static Platform platform() {
        return Platform.builder()
                .id(UUID.randomUUID())
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
    }
}
