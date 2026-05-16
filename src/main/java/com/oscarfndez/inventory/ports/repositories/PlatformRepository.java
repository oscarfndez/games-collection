package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.mappers.PlatformEntityModelMapper;
import com.oscarfndez.inventory.adapters.persistence.exceptions.ResourceNotFoundException;
import com.oscarfndez.inventory.core.model.Platform;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class PlatformRepository {

    private final PlatformJpaRepository platformJpaRepository;
    private final PlatformEntityModelMapper platformEntityModelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public Platform retrieveOne(UUID id) {
        return platformJpaRepository.findByIdAndDeletedFalse(id)
                .map(platformEntityModelMapper::entityToModel)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Platform> retrieveAny() {
        return platformJpaRepository.findAllByDeletedFalse()
                .stream()
                .map(platformEntityModelMapper::entityToModel)
                .toList();
    }

    public List<Platform> retrieveMany(List<UUID> ids) {
        List<Platform> platforms = platformJpaRepository.findAllByIdInAndDeletedFalse(ids)
                .stream()
                .map(platformEntityModelMapper::entityToModel)
                .toList();

        if (platforms.size() != ids.size()) {
            throw new ResourceNotFoundException();
        }

        Map<UUID, Platform> platformsById = platforms.stream()
                .collect(Collectors.toMap(Platform::getId, Function.identity()));

        return ids.stream()
                .map(platformsById::get)
                .toList();
    }

    public Platform save(Platform platform) {
        return platformEntityModelMapper.entityToModel(
                platformJpaRepository.save(platformEntityModelMapper.modelToEntity(platform))
        );
    }

    public void deleteOne(UUID id) {
        if (platformJpaRepository.softDeleteById(id) == 0) {
            throw new ResourceNotFoundException();
        }
    }

    public Page<Platform> search(String search, Pageable pageable) {
        return platformJpaRepository.search(search, pageable)
                .map(platformEntityModelMapper::entityToModel);
    }

    public List<Platform> findAllSorted(String sortField, boolean asc) {
        String direction = asc ? "asc" : "desc";

        String query = """
            select p
            from PlatformEntity p
            where p.deleted = false
            """ + " order by " + sortField + " " + direction;

        return entityManager.createQuery(query, PlatformEntity.class)
                .getResultList()
                .stream()
                .map(platformEntityModelMapper::entityToModel)
                .toList();
    }

    public List<Platform> searchAndSort(String search, String sortField, boolean asc) {
        String direction = asc ? "asc" : "desc";

        String query = """
            select p
            from PlatformEntity p
            where p.deleted = false
              and (lower(p.name) like lower(concat('%', :search, '%'))
               or lower(p.description) like lower(concat('%', :search, '%')))
            """ + " order by " + sortField + " " + direction;

        return entityManager.createQuery(query, PlatformEntity.class)
                .setParameter("search", search)
                .getResultList()
                .stream()
                .map(platformEntityModelMapper::entityToModel)
                .toList();
    }
}
