package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.inventory.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformModelDtoMapper {

    public PlatformDto mapToDTO(Platform platform) {
        return PlatformDto.builder().id(platform.getId())
                .name(platform.getName())
                .description(platform.getDescription())
                .imageUrl(platform.getImageUrl())
                .build();
    }
}
