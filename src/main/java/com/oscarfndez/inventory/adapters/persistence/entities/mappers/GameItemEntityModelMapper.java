package com.oscarfndez.inventory.adapters.persistence.entities.mappers;

import com.oscarfndez.inventory.adapters.persistence.entities.GameItemEntity;
import com.oscarfndez.inventory.core.model.GameItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameItemEntityModelMapper {

    private final GameEntityModelMapper gameEntityModelMapper;
    private final PlatformEntityModelMapper platformEntityModelMapper;

    public GameItem entityToModel(GameItemEntity entity) {
        return new GameItem(
                entity.getId(),
                entity.getUserId(),
                gameEntityModelMapper.entityToModel(entity.getGame()),
                platformEntityModelMapper.entityToModel(entity.getPlatform()),
                entity.isActive()
        );
    }
}
