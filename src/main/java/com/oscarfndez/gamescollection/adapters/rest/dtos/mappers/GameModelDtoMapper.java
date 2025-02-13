package com.oscarfndez.gamescollection.adapters.rest.dtos.mappers;

import com.oscarfndez.framework.adapters.rest.dtos.mappers.ModelDtoMapper;
import com.oscarfndez.gamescollection.adapters.rest.dtos.GameDto;
import com.oscarfndez.gamescollection.core.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameModelDtoMapper implements ModelDtoMapper<Game, GameDto> {

    public GameDto mapToDTO(Game game) {
        return GameDto.builder().id(game.getId())
                .name(game.getName())
                .description(game.getDescription())
                .platformId(game.getPlatform().getId())
                .build();
    }
}
