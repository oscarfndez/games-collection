package com.oscarfndez.gamescollection.adapters.persistence.entities.mappers;

import com.oscarfndez.framework.adapters.persistence.ModelEntityMapper;
import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformEntityModelMapper implements ModelEntityMapper<Platform, PlatformEntity> {

    public PlatformEntity modelToEntity(Platform platform) {
        return new PlatformEntity(platform.getId(), platform.getName(), platform.getDescription());
    }

    public Platform entityToModel(PlatformEntity platformEntity) {
        return new Platform(platformEntity.getId(), platformEntity.getName(), platformEntity.getDescription());
    }
}