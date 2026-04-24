package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.users.adapters.rest.dtos.mappers.ModelDtoMapper;
import com.oscarfndez.inventory.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.inventory.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformModelDtoMapper implements ModelDtoMapper<Platform, PlatformDto> {

    public PlatformDto mapToDTO(Platform platform) {
        return PlatformDto.builder().id(platform.getId())
                .name(platform.getName())
                .description(platform.getDescription())
                .imageUrl(platform.getImageUrl())
                .build();
    }
}
