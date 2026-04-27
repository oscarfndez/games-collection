package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameEntityModelMapperTest {

    private final GameEntityModelMapper mapper = new GameEntityModelMapper(new PlatformEntityModelMapper());

    @Test
    void modelToEntityMapsGameAndPlatformFields() {
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

        GameEntity entity = mapper.modelToEntity(game);

        assertThat(entity.getId()).isEqualTo(gameId);
        assertThat(entity.getName()).isEqualTo("Zelda");
        assertThat(entity.getDescription()).isEqualTo("Adventure");
        assertThat(entity.getImageUrl()).isEqualTo("zelda.png");
        assertThat(entity.getPlatform().getId()).isEqualTo(platformId);
        assertThat(entity.getPlatform().getName()).isEqualTo("Nintendo Switch");
    }

    @Test
    void entityToModelMapsGameAndPlatformFields() {
        UUID platformId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        PlatformEntity platform = new PlatformEntity(platformId, "Nintendo Switch", "Console", "switch.png");
        GameEntity entity = new GameEntity(gameId, "Zelda", "Adventure", "zelda.png", platform);

        Game game = mapper.entityToModel(entity);

        assertThat(game.getId()).isEqualTo(gameId);
        assertThat(game.getName()).isEqualTo("Zelda");
        assertThat(game.getDescription()).isEqualTo("Adventure");
        assertThat(game.getImageUrl()).isEqualTo("zelda.png");
        assertThat(game.getPlatform().getId()).isEqualTo(platformId);
        assertThat(game.getPlatform().getName()).isEqualTo("Nintendo Switch");
    }
}
