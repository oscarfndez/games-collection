package com.oscarfndez.gamescollection.ports.repositories;

import com.oscarfndez.framework.adapters.persistence.mappers.HexagonalRepository;
import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.core.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class PlatformRepository extends HexagonalRepository<Platform, PlatformEntity> {

}
