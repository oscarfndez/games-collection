package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.adapters.rest.dtos.StudioDto;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Studio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudioModelDtoMapper {

    public StudioDto mapToDTO(Studio studio) {
        List<Game> games = studio.getGames() == null ? List.of() : studio.getGames();

        return StudioDto.builder()
                .id(studio.getId())
                .name(studio.getName())
                .description(studio.getDescription())
                .location(studio.getLocation())
                .firstParty(studio.getFirstParty())
                .gameIds(games.stream().map(Game::getId).toList())
                .gameNames(games.stream().map(Game::getName).toList())
                .gamesCount(games.size())
                .build();
    }
}
