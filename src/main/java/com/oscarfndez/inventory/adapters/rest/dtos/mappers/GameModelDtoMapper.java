package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.adapters.rest.dtos.GameDto;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameModelDtoMapper {

    public GameDto mapToDTO(Game game) {
        List<Platform> platforms = game.getPlatforms() == null ? List.of() : game.getPlatforms();
        List<java.util.UUID> platformIds = platforms.stream().map(Platform::getId).toList();
        List<String> platformNames = platforms.stream().map(Platform::getName).toList();
        Platform firstPlatform = platforms.stream().findFirst().orElse(null);

        return GameDto.builder().id(game.getId())
                .name(game.getName())
                .description(game.getDescription())
                .platformId(firstPlatform == null ? null : firstPlatform.getId())
                .platformName(platforms.size() > 1
                        ? "Multiplataforma"
                        : firstPlatform == null ? null : firstPlatform.getName())
                .platformIds(platformIds)
                .platformNames(platformNames)
                .imageUrl(game.getImageUrl())
                .studioId(game.getStudio() == null ? null : game.getStudio().getId())
                .studioName(game.getStudio() == null ? null : game.getStudio().getName())
                .build();
    }
}
