package com.oscarfndez.inventory.adapters.rest.dtos.mappers;

import com.oscarfndez.inventory.adapters.rest.dtos.GameItemDto;
import com.oscarfndez.inventory.core.model.GameItem;
import org.springframework.stereotype.Component;

@Component
public class GameItemModelDtoMapper {

    public GameItemDto mapToDTO(GameItem gameItem) {
        return GameItemDto.builder()
                .id(gameItem.getId())
                .userId(gameItem.getUserId())
                .gameId(gameItem.getGame().getId())
                .gameName(gameItem.getGame().getName())
                .gameImageUrl(gameItem.getGame().getImageUrl())
                .platformId(gameItem.getPlatform().getId())
                .platformName(gameItem.getPlatform().getName())
                .build();
    }
}
