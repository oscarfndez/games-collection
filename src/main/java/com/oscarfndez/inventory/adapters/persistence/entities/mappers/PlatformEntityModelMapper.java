package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformEntityModelMapper {

    public PlatformEntity modelToEntity(Platform platform) {
        return new PlatformEntity(platform.getId(), platform.getName(), platform.getDescription(), platform.getImageUrl());
    }

    public Platform entityToModel(PlatformEntity platformEntity) {
        return new Platform(platformEntity.getId(), platformEntity.getName(), platformEntity.getDescription(), platformEntity.getImageUrl());
    }
}
