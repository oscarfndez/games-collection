package com.gamescollection.ports.repositories;

import com.framework.architecture.hexagonal.persistence.HexagonalRepository;
import com.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.gamescollection.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformRepository extends HexagonalRepository<Platform, PlatformEntity> {

}
