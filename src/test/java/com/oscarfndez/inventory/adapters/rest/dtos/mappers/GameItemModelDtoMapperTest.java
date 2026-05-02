package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.GameItem;
import com.oscarfndez.inventory.core.model.Platform;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameItemModelDtoMapperTest {

    private final GameItemModelDtoMapper mapper = new GameItemModelDtoMapper();

    @Test
    void mapToDtoIncludesGameImageUrl() {
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
        GameItem gameItem = GameItem.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .game(game)
                .platform(platform)
                .active(true)
                .build();

        var dto = mapper.mapToDTO(gameItem);

        assertThat(dto.getGameImageUrl()).isEqualTo("elden-ring.png");
    }
}
