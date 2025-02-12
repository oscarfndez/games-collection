package com.gamescollection.adapters.persistence;

import com.gamescollection.adapters.persistence.entities.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlatformJpaRepository extends JpaRepository<PlatformEntity, UUID> {
}
