package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.core.model.Platform;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformModelDtoMapperTest {

    private final PlatformModelDtoMapper mapper = new PlatformModelDtoMapper();

    @Test
    void mapToDTOMapsPlatformFields() {
        UUID platformId = UUID.randomUUID();
        Platform platform = Platform.builder()
                .id(platformId)
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();

        var dto = mapper.mapToDTO(platform);

        assertThat(dto.getId()).isEqualTo(platformId);
        assertThat(dto.getName()).isEqualTo("Nintendo Switch");
        assertThat(dto.getDescription()).isEqualTo("Console");
        assertThat(dto.getImageUrl()).isEqualTo("switch.png");
    }
}
