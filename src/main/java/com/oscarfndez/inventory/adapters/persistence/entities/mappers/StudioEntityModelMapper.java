package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.StudioEntity;
import com.oscarfndez.inventory.core.model.Game;
import com.oscarfndez.inventory.core.model.Platform;
import com.oscarfndez.inventory.core.model.Studio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudioEntityModelMapper {

    public StudioEntity modelToEntity(Studio studio) {
        return new StudioEntity(
                studio.getId(),
                studio.getName(),
                studio.getDescription(),
                studio.getLocation(),
                studio.getFirstParty(),
                new ArrayList<>()
        );
    }

    public Studio entityToModel(StudioEntity studioEntity) {
        return new Studio(
                studioEntity.getId(),
                studioEntity.getName(),
                studioEntity.getDescription(),
                studioEntity.getLocation(),
                studioEntity.getFirstParty(),
                studioEntity.getGames() == null
                        ? List.of()
                        : studioEntity.getGames().stream()
                                .map(gameEntity -> Game.builder()
                                        .id(gameEntity.getId())
                                        .name(gameEntity.getName())
                                        .description(gameEntity.getDescription())
                                        .imageUrl(gameEntity.getImageUrl())
                                        .platforms(gameEntity.getPlatforms() == null
                                                ? List.of()
                                                : gameEntity.getPlatforms().stream()
                                                        .map(platformEntity -> Platform.builder()
                                                                .id(platformEntity.getId())
                                                                .name(platformEntity.getName())
                                                                .description(platformEntity.getDescription())
                                                                .imageUrl(platformEntity.getImageUrl())
                                                                .build())
                                                        .toList())
                                        .build())
                                .toList()
        );
    }

    public Studio entityToModelWithoutGames(StudioEntity studioEntity) {
        return new Studio(
                studioEntity.getId(),
                studioEntity.getName(),
                studioEntity.getDescription(),
                studioEntity.getLocation(),
                studioEntity.getFirstParty(),
                List.of()
        );
    }
}
