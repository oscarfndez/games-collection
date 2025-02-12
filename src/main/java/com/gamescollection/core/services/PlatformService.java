package com.gamescollection.core.services;

import com.framework.architecture.hexagonal.persistence.HexagonalRepository;
import com.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.gamescollection.core.model.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PlatformService {

    private final HexagonalRepository<Platform, PlatformEntity> platformRepository;

    public Platform retrieveOne(UUID id) {
        return platformRepository.retrieveOne(id);
    }

    public List<Platform> retrieveAny() {
        return platformRepository.retrieveAny();
    }

    public Platform createPlatform(String name, String description) {
        return platformRepository.save(Platform.builder().id(UUID.randomUUID()).name(name).description(description).build());
    }

    public Platform updatePlatform(UUID id, String name, String description) {
        return platformRepository.save(Platform.builder().id(id).name(name).description(description).build());
    }
}
