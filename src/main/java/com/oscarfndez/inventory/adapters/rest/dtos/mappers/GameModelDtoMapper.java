package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.adapters.rest.dtos.GameDto;
import com.oscarfndez.inventory.core.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameModelDtoMapper {

    public GameDto mapToDTO(Game game) {
        return GameDto.builder().id(game.getId())
                .name(game.getName())
                .description(game.getDescription())
                .platformId(game.getPlatform().getId())
                .platformName(game.getPlatform().getName())
                .imageUrl(game.getImageUrl())
                .build();
    }
}
