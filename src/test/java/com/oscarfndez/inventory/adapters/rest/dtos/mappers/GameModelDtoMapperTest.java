package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameModelDtoMapperTest {

    private final GameModelDtoMapper mapper = new GameModelDtoMapper();

    @Test
    void mapToDTOMapsGameAndPlatformFields() {
        UUID platformId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        Platform platform = Platform.builder()
                .id(platformId)
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
        Game game = Game.builder()
                .id(gameId)
                .name("Zelda")
                .description("Adventure")
                .platform(platform)
                .imageUrl("zelda.png")
                .build();

        var dto = mapper.mapToDTO(game);

        assertThat(dto.getId()).isEqualTo(gameId);
        assertThat(dto.getName()).isEqualTo("Zelda");
        assertThat(dto.getDescription()).isEqualTo("Adventure");
        assertThat(dto.getPlatformId()).isEqualTo(platformId);
        assertThat(dto.getPlatformName()).isEqualTo("Nintendo Switch");
        assertThat(dto.getImageUrl()).isEqualTo("zelda.png");
    }
}
