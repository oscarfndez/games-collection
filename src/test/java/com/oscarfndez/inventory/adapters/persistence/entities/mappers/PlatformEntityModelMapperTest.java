package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.core.model.Platform;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformEntityModelMapperTest {

    private final PlatformEntityModelMapper mapper = new PlatformEntityModelMapper();

    @Test
    void modelToEntityMapsFields() {
        UUID platformId = UUID.randomUUID();
        Platform platform = Platform.builder()
                .id(platformId)
                .name("Steam")
                .description("Store")
                .imageUrl("steam.png")
                .build();

        PlatformEntity entity = mapper.modelToEntity(platform);

        assertThat(entity.getId()).isEqualTo(platformId);
        assertThat(entity.getName()).isEqualTo("Steam");
        assertThat(entity.getDescription()).isEqualTo("Store");
        assertThat(entity.getImageUrl()).isEqualTo("steam.png");
    }

    @Test
    void entityToModelMapsFields() {
        UUID platformId = UUID.randomUUID();
        PlatformEntity entity = new PlatformEntity(platformId, "Steam", "Store", "steam.png");

        Platform platform = mapper.entityToModel(entity);

        assertThat(platform.getId()).isEqualTo(platformId);
        assertThat(platform.getName()).isEqualTo("Steam");
        assertThat(platform.getDescription()).isEqualTo("Store");
        assertThat(platform.getImageUrl()).isEqualTo("steam.png");
    }
}
