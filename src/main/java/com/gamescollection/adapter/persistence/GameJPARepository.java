package com.gamescollection.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameJPARepository extends JpaRepository<GameEntity, UUID> {
}
