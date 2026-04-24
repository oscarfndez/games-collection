package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.framework.adapters.persistence.repositories.HexagonalRepository;
import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.core.model.Game;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Component
public class GameRepository extends HexagonalRepository<Game, GameEntity> {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GameJpaRepository gameJpaRepository;

    public List<Game> search(String search) {
        return gameJpaRepository.search(search)
                .stream()
                .map(modelEntityMapper::entityToModel)
                .toList();
    }


    public Page<Game> search(String search, Pageable pageable) {
        return gameJpaRepository.search(search, pageable)
                .map(modelEntityMapper::entityToModel);

    }

    public List<Game> findAllSorted(String sortField, boolean asc) {

        String direction = asc ? "asc" : "desc";

        String query = """
        select g
        from GameEntity g
        join g.platform p
        """ + " order by " + sortField + " " + direction;

        return entityManager.createQuery(query, GameEntity.class)
                .getResultList()
                .stream()
                .map(modelEntityMapper::entityToModel)
                .toList();
    }

    public List<Game> searchAndSort(String search, String sortField, boolean asc) {

        String direction = asc ? "asc" : "desc";

        String query = """
        select g
        from GameEntity g
        join g.platform p
        where lower(g.name) like lower(concat('%', :search, '%'))
           or lower(g.description) like lower(concat('%', :search, '%'))
           or lower(p.name) like lower(concat('%', :search, '%'))
        """ + " order by " + sortField + " " + direction;

        return entityManager.createQuery(query, GameEntity.class)
                .setParameter("search", search)
                .getResultList()
                .stream()
                .map(modelEntityMapper::entityToModel)
                .toList();
    }
}