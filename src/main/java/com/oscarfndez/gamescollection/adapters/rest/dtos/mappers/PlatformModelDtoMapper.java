package com.oscarfndez.gamescollection.adapters.rest.dtos.mappers;

import com.oscarfndez.framework.adapters.rest.dtos.mappers.ModelDtoMapper;
import com.oscarfndez.gamescollection.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.gamescollection.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformModelDtoMapper implements ModelDtoMapper<Platform, PlatformDto> {

    public PlatformDto mapToDTO(Platform platform) {
        return PlatformDto.builder().id(platform.getId())
                .name(platform.getName())
                .description(platform.getDescription())
                .build();
    }
}
