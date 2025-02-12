package com.gamescollection.adapters.rest.dtos.mappers;

import com.framework.architecture.hexagonal.utils.ModelDtoMapper;
import com.gamescollection.adapters.rest.dtos.PlatformDto;
import com.gamescollection.core.model.Platform;
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
