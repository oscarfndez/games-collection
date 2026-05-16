package com.oscarfndez.inventory.core.services;

import com.oscarfndez.inventory.core.events.InventoryEntityDeletedEventPublisher;
import com.oscarfndez.inventory.core.model.Studio;
import com.oscarfndez.inventory.ports.repositories.StudioRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class StudioService {

    private final StudioRepository studioRepository;
    private final InventoryEntityDeletedEventPublisher inventoryEntityDeletedEventPublisher;

    @Cacheable(cacheNames = "studios", key = "#p0")
    public Studio retrieveOne(UUID id) {
        return studioRepository.retrieveOne(id);
    }

    @Cacheable(cacheNames = "studioPages", key = "{#p0 == null ? '' : #p0.trim(), #p1, #p2, #p3, #p4}")
    public Page<Studio> retrievePage(String search, String sortField, String sortDir, int page, int size) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, mapSortField(sortField)));
        return studioRepository.search(search == null ? "" : search.trim(), pageable);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "studioPages", allEntries = true),
            @CacheEvict(cacheNames = "gamePages", allEntries = true)
    })
    public Studio createStudio(String name, String description, String location, Boolean firstParty) {
        return studioRepository.save(Studio.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .location(location)
                .firstParty(firstParty)
                .games(java.util.List.of())
                .build());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "studios", key = "#p0"),
            @CacheEvict(cacheNames = "studioPages", allEntries = true),
            @CacheEvict(cacheNames = "gamePages", allEntries = true)
    })
    public Studio updateStudio(UUID id, String name, String description, String location, Boolean firstParty) {
        return studioRepository.save(Studio.builder()
                .id(id)
                .name(name)
                .description(description)
                .location(location)
                .firstParty(firstParty)
                .games(java.util.List.of())
                .build());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "studios", key = "#p0"),
            @CacheEvict(cacheNames = "studioPages", allEntries = true),
            @CacheEvict(cacheNames = "gamePages", allEntries = true)
    })
    public void deleteOne(UUID id) {
        Studio studio = studioRepository.retrieveOne(id);
        studioRepository.deleteOne(id);
        inventoryEntityDeletedEventPublisher.studioDeleted(studio.getId(), studio.getName());
    }

    private String mapSortField(String sortField) {
        return switch (sortField) {
            case "description" -> "description";
            case "location" -> "location";
            case "firstParty" -> "firstParty";
            default -> "name";
        };
    }
}
