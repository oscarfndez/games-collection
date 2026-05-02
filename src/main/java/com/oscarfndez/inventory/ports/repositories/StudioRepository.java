package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.mappers.StudioEntityModelMapper;
import com.oscarfndez.inventory.adapters.persistence.exceptions.ResourceNotFoundException;
import com.oscarfndez.inventory.core.model.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudioRepository {

    private final StudioJpaRepository studioJpaRepository;
    private final StudioEntityModelMapper studioEntityModelMapper;

    public Studio retrieveOne(UUID id) {
        var entity = studioJpaRepository.findOneWithGames(id);
        if (entity == null) {
            throw new ResourceNotFoundException();
        }

        return studioEntityModelMapper.entityToModel(entity);
    }

    public Studio save(Studio studio) {
        return studioEntityModelMapper.entityToModel(
                studioJpaRepository.save(studioEntityModelMapper.modelToEntity(studio))
        );
    }

    public Page<Studio> search(String search, Pageable pageable) {
        return studioJpaRepository.search(search, pageable)
                .map(studioEntityModelMapper::entityToModelWithoutGames);
    }

    public void deleteOne(UUID id) {
        studioJpaRepository.deleteById(id);
    }
}
