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
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlatformRepository {

    private final PlatformJpaRepository platformJpaRepository;
    private final PlatformEntityModelMapper platformEntityModelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public Platform retrieveOne(UUID id) {
        try {
            return platformEntityModelMapper.entityToModel(platformJpaRepository.getReferenceById(id));
        } catch (JpaObjectRetrievalFailureException e) {
            throw new ResourceNotFoundException();
        }
    }

    public List<Platform> retrieveAny() {
        return platformJpaRepository.findAll()
                .stream()
                .map(platformEntityModelMapper::entityToModel)
                .toList();
    }

    public Platform save(Platform platform) {
        return platformEntityModelMapper.entityToModel(
                platformJpaRepository.save(platformEntityModelMapper.modelToEntity(platform))
        );
    }

    public void deleteOne(UUID id) {
        platformJpaRepository.deleteById(id);
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
            where lower(p.name) like lower(concat('%', :search, '%'))
               or lower(p.description) like lower(concat('%', :search, '%'))
            """ + " order by " + sortField + " " + direction;

        return entityManager.createQuery(query, PlatformEntity.class)
                .setParameter("search", search)
                .getResultList()
                .stream()
                .map(platformEntityModelMapper::entityToModel)
                .toList();
    }
}
