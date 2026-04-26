package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.mappers.GameEntityModelMapper;
import com.oscarfndez.inventory.adapters.persistence.exceptions.ResourceNotFoundException;
import com.oscarfndez.inventory.core.model.Game;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Component
@RequiredArgsConstructor
public class GameRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final GameJpaRepository gameJpaRepository;
    private final GameEntityModelMapper gameEntityModelMapper;

    public Game retrieveOne(UUID id) {
        try {
            return gameEntityModelMapper.entityToModel(gameJpaRepository.getReferenceById(id));
        } catch (JpaObjectRetrievalFailureException e) {
            throw new ResourceNotFoundException();
        }
    }

    public List<Game> retrieveAny() {
        return gameJpaRepository.findAll()
                .stream()
                .map(gameEntityModelMapper::entityToModel)
                .toList();
    }

    public Game save(Game game) {
        return gameEntityModelMapper.entityToModel(
                gameJpaRepository.save(gameEntityModelMapper.modelToEntity(game))
        );
    }

    public void deleteOne(UUID id) {
        gameJpaRepository.deleteById(id);
    }

    public List<Game> search(String search) {
        return gameJpaRepository.search(search)
                .stream()
                .map(gameEntityModelMapper::entityToModel)
                .toList();
    }


    public Page<Game> search(String search, Pageable pageable) {
        return gameJpaRepository.search(search, pageable)
                .map(gameEntityModelMapper::entityToModel);

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
                .map(gameEntityModelMapper::entityToModel)
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
                .map(gameEntityModelMapper::entityToModel)
                .toList();
    }
}
