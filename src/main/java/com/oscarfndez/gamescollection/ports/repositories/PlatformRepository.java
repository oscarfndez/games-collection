package com.oscarfndez.gamescollection.ports.repositories;

import com.oscarfndez.framework.adapters.persistence.mappers.HexagonalRepository;
import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.core.model.Platform;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlatformRepository extends HexagonalRepository<Platform, PlatformEntity> {

    @Autowired
    private PlatformJpaRepository platformJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Platform> search(String search) {
        return platformJpaRepository.search(search)
                .stream()
                .map(modelEntityMapper::entityToModel)
                .toList();
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
                .map(modelEntityMapper::entityToModel)
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
                .map(modelEntityMapper::entityToModel)
                .toList();
    }
}