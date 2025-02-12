package com.gamescollection.adapters.persistence.entities.mappers;

import com.framework.architecture.hexagonal.persistence.ModelEntityMapper;
import com.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.gamescollection.core.model.Platform;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PlatformEntityModelMapper implements ModelEntityMapper<Platform, PlatformEntity> {

    public PlatformEntity modelToEntity(Platform platform) {
        return new PlatformEntity(platform.getId(), platform.getName(), platform.getDescription());
    }

    public Platform entityToModel(PlatformEntity platformEntity) {
        return new Platform(platformEntity.getId(), platformEntity.getName(), platformEntity.getDescription());
    }
}