package com.oscarfndez.inventory.ports.repositories;

import com.oscarfndez.inventory.adapters.persistence.entities.GameEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.StudioEntity;
import com.oscarfndez.inventory.adapters.persistence.entities.mappers.GameEntityModelMapper;
import com.oscarfndez.inventory.adapters.persistence.exceptions.ResourceNotFoundException;
import com.oscarfndez.inventory.core.model.Game;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Component
@RequiredArgsConstructor
@Transactional
public class GameRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final GameJpaRepository gameJpaRepository;
    private final GameEntityModelMapper gameEntityModelMapper;

    public Game retrieveOne(UUID id) {
        return gameJpaRepository.findByIdAndDeletedFalse(id)
                .map(gameEntityModelMapper::entityToModel)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Game> retrieveAny() {
        return gameJpaRepository.findAllByDeletedFalse()
                .stream()
                .map(gameEntityModelMapper::entityToModel)
                .toList();
    }

    public Game save(Game game) {
        GameEntity entity = gameEntityModelMapper.modelToEntity(game);
        entity.setPlatforms(game.getPlatforms().stream()
                .map(platform -> entityManager.getReference(PlatformEntity.class, platform.getId()))
                .toList());
        entity.setStudio(game.getStudio() == null
                ? null
                : entityManager.getReference(StudioEntity.class, game.getStudio().getId()));

        return gameEntityModelMapper.entityToModel(
                gameJpaRepository.save(entity)
        );
    }

    public void deleteOne(UUID id) {
        if (gameJpaRepository.softDeleteById(id) == 0) {
            throw new ResourceNotFoundException();
        }
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

    public Page<Game> searchByPlatformId(UUID platformId, String search, Pageable pageable) {
        return gameJpaRepository.searchByPlatformId(platformId, search, pageable)
                .map(gameEntityModelMapper::entityToModel);
    }

    public Page<Game> searchByStudioId(UUID studioId, String search, Pageable pageable) {
        return gameJpaRepository.searchByStudioId(studioId, search, pageable)
                .map(gameEntityModelMapper::entityToModel);
    }

    public List<Game> findAllSorted(String sortField, boolean asc) {

        String direction = asc ? "asc" : "desc";
        String querySortField = mapQuerySortField(sortField);

        String query = """
        select distinct g
        from GameEntity g
        join g.platforms p
        where g.deleted = false
          and p.deleted = false
        """ + " order by " + querySortField + " " + direction;

        return entityManager.createQuery(query, GameEntity.class)
                .getResultList()
                .stream()
                .map(gameEntityModelMapper::entityToModel)
                .toList();
    }

    public List<Game> searchAndSort(String search, String sortField, boolean asc) {

        String direction = asc ? "asc" : "desc";
        String querySortField = mapQuerySortField(sortField);

        String query = """
        select distinct g
        from GameEntity g
        join g.platforms p
        where g.deleted = false
          and p.deleted = false
          and (lower(g.name) like lower(concat('%', :search, '%'))
           or lower(g.description) like lower(concat('%', :search, '%'))
           or lower(p.name) like lower(concat('%', :search, '%')))
        """ + " order by " + querySortField + " " + direction;

        return entityManager.createQuery(query, GameEntity.class)
                .setParameter("search", search)
                .getResultList()
                .stream()
                .map(gameEntityModelMapper::entityToModel)
                .toList();
    }

    private String mapQuerySortField(String sortField) {
        return switch (sortField) {
            case "description" -> "g.description";
            case "platforms.name" -> "p.name";
            default -> "g.name";
        };
    }
}
