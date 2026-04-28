package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.GameItemEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.mappers.GameItemEntityModelMapper;
import com.oscarfndez.inventory.core.model.GameItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
public class GameItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final GameItemJpaRepository gameItemJpaRepository;
    private final GameItemEntityModelMapper gameItemEntityModelMapper;

    public List<GameItem> findByUserId(UUID userId) {
        return gameItemJpaRepository.findByUserId(userId)
                .stream()
                .map(gameItemEntityModelMapper::entityToModel)
                .toList();
    }

    public GameItem save(UUID id, UUID userId, UUID gameId, UUID platformId) {
        GameItemEntity entity = new GameItemEntity(
                id,
                userId,
                entityManager.getReference(GameEntity.class, gameId),
                entityManager.getReference(PlatformEntity.class, platformId)
        );

        return gameItemEntityModelMapper.entityToModel(gameItemJpaRepository.save(entity));
    }

    public void deleteOne(UUID id, UUID userId) {
        gameItemJpaRepository.deleteByIdAndUserId(id, userId);
    }
}
